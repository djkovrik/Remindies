package com.sedsoftware.core.domain.repository

import com.sedsoftware.core.domain.entity.Remindie

interface RemindiesRepository {
    fun insert(remindie: Remindie)
    fun delete(remindie: Remindie)
    fun getAll(): List<Remindie>
}
