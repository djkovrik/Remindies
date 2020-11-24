package com.sedsoftware.common.domain.repository

import com.sedsoftware.common.domain.entity.Remindie

interface RemindiesRepository {
    fun insert(remindie: Remindie)
    fun delete(remindie: Remindie)
    fun getAll(): List<Remindie>
}
