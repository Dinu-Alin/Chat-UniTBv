package com.lagar.chatunitbv.ui.items
import Operations
import android.view.LayoutInflater
import android.view.ViewGroup
import coil.transform.CircleCropTransformation
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.PeopleItemLayoutBinding
import com.lagar.chatunitbv.models.People
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import io.github.rosariopfernandes.firecoil.load

class PeopleItem(val people: People?):
    AbstractBindingItem<PeopleItemLayoutBinding>(){

    override val type: Int
        get() = R.id.people_item

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): PeopleItemLayoutBinding {
        return PeopleItemLayoutBinding.inflate(inflater, parent, false)
    }
    override fun bindView(binding: PeopleItemLayoutBinding, payloads: List<Any>) {

        binding.peopleName.text = people?.name ?: ""

        val femaleAvatar = Operations.storage.getReference("images/female.png");
        val maleAvatar = Operations.storage.getReference("images/male.png");
        if (people != null) {
           if( people.gender=="female"){
               binding.peoplePhotoRv.load(femaleAvatar) {
                   crossfade(true)
                   transformations(CircleCropTransformation())
               }
           }else{
               binding.peoplePhotoRv.load(maleAvatar) {
                   crossfade(true)
                   transformations(CircleCropTransformation())
               }
           }
        }

    }
}