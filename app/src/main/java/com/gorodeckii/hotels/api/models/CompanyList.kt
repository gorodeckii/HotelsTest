package com.gorodeckii.hotels.api.models

data class CompanyList(val companies: Array<Company>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompanyList

        if (!companies.contentEquals(other.companies)) return false

        return true
    }

    override fun hashCode(): Int {
        return companies.contentHashCode()
    }

}