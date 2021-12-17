import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

object Operations{

    val db by lazy { Firebase.firestore }
    val storage by lazy { Firebase.storage}
}