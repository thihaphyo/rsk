package com.rit.domain.models

interface DomainModel<T> {
    fun areItemsTheSame(item: T): Boolean
    fun areContentsTheSame(item: T): Boolean
}