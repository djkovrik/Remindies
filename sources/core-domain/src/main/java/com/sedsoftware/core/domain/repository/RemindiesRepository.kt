package com.sedsoftware.core.domain.repository

import com.sedsoftware.core.domain.entity.Remindie

interface RemindiesRepository {
    suspend fun insert(remindie: Remindie)
    suspend fun delete(remindie: Remindie)
}
