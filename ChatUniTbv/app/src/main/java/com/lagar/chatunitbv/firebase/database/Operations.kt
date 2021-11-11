package com.lagar.chatunitbv.firebase.database

import android.annotation.SuppressLint
import com.google.firebase.FirebaseException
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resumeWithException

object Operations {


    val database = Firebase.firestore

    const val TAG: String = "Operations"

    suspend fun DatabaseReference.awaitsSingle(): DataSnapshot? =
        suspendCancellableCoroutine { continuation ->
            val listener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    val exception = when (error.toException()) {
                        is FirebaseException -> error.toException()
                        else -> Exception("The Firebase call for reference $this was cancelled")
                    }
                    continuation.resumeWithException(exception)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        continuation.resume(snapshot) {
                            removeEventListener(this)
                        }
                    } catch (exception: Exception) {
                        continuation.resumeWithException(exception)
                    }
                }
            }
            continuation.invokeOnCancellation { this.removeEventListener(listener) }
            this.addListenerForSingleValueEvent(listener)
        }


    fun DatabaseReference.observeValue(): Flow<DataSnapshot?> =
        callbackFlow {
            val listener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    this@callbackFlow.trySend(snapshot).isSuccess
                }
            }
            addValueEventListener(listener)
            awaitClose { removeEventListener(listener) }
        }

    fun DatabaseReference.observeChildEvent(): Flow<DataSnapshot?> {
        return callbackFlow {
            val listener = object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    Timber.i("Child moved for database reference: ${snapshot.ref}")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    offer(snapshot)
                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    offer(snapshot)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    offer(snapshot)
                }
            }

            addChildEventListener(listener)
            awaitClose { removeEventListener(listener) }
        }
    }
}