package com.example.techexac.Model

data class ApplicationsResponse(
    var success: Boolean,
    var data: ApplicationsList? = null,
    var message: String? = null,
)

data class ApplicationsList(
    var app_list: ArrayList<AppList>? = null,
    var usage_access: Int? = 0,
)

data class AppList(
    var app_id: Int? = 0,
    var fk_kid_id: Int? = 0,
    var kid_profile_image: String? = null,
    var app_name: String? = null,
    var app_icon: String? = null,
    var app_package_name: String? = null,
    var status: String? = null,
    var notification: Boolean? = false,
)
