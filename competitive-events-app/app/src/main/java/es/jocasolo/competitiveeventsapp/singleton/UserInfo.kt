package es.jocasolo.competitiveeventsapp.singleton

import android.content.Context
import es.jocasolo.competitiveeventsapp.dto.user.ImageDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO

class UserInfo private constructor(context: Context) {

    private var userDto : UserDTO? = null

    fun setUserDTO(userDto : UserDTO?){
        this.userDto = userDto
    }

    fun getUserDTO() : UserDTO? {
        return userDto
    }

    fun getId() : String {
        return userDto?.id!!
    }

    fun getAvatar() : ImageDTO? {
        return userDto?.avatar
    }

    fun getName() : String? {
        return userDto?.name
    }

    fun getSurname() : String? {
        return userDto?.surname
    }

    fun getDescription() : String? {
        return userDto?.description
    }

    fun getEmail() : String? {
        return userDto?.email
    }

    companion object : SingletonHolder<UserInfo, Context>(::UserInfo)

}