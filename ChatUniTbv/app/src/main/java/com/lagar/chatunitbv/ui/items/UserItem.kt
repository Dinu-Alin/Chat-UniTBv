package com.lagar.chatunitbv.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.transform.CircleCropTransformation
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.PeopleItemLayoutBinding
import com.lagar.chatunitbv.firebase.Operations
import com.lagar.chatunitbv.models.User
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import io.github.rosariopfernandes.firecoil.load

class UserItem(val user: User?) :
    AbstractBindingItem<PeopleItemLayoutBinding>() {

    override val type: Int
        get() = R.id.people_item

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): PeopleItemLayoutBinding {
        return PeopleItemLayoutBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: PeopleItemLayoutBinding, payloads: List<Any>) {

        binding.peopleName.text = user?.name ?: ""

        val avatar = Operations.store.getReference("images/avatar.jpg")


        val reference = Operations.store.getReference("images/users/${user?.email}.jpg")

        reference.downloadUrl.addOnSuccessListener {
            binding.peoplePhotoRv.load(reference) {
                //  crossfade(true)
//                placeholder(R.drawable.ic_baseline_person_24)
                transformations(CircleCropTransformation())
            }
        }
            .addOnFailureListener {
                if (user != null) {

                    binding.peoplePhotoRv.load(avatar) {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                    }
                }
            }
    }
}
