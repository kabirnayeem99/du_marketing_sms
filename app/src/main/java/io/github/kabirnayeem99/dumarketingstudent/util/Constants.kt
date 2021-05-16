package io.github.kabirnayeem99.dumarketingstudent.util

import io.github.kabirnayeem99.dumarketingstudent.util.enums.FacultyPosts

object Constants {
    const val ABOUT_DB_PATH: String = "About"
    const val LATITUDE: String = "latitude"
    const val LONGTITUDE: String = "longtitude"
    const val FACULTY_STORAGE_PATH: String = "Faculty"
    const val EBOOK_STORAGE_PATH: String = "Ebook"
    const val ABOUT_DB_REF: String = "About"
    const val EBOOK_DB_REF: String = "Ebook"
    const val ROUTINE_DB_REF: String = "Routine"
    const val FACULTY_DB_COLLECTION_NAME: String = "Faculty"
    const val NOTICE_IMAGE_PATH_STRING_FOLDER_NAME = "Notice"
    const val GALLERY_IMAGE_PATH_STRING_FOLDER_NAME = "Gallery"
    const val NOTICE_DB_REF = "Notice"
    const val GALLERY_DB_REF = "Gallery"
    const val CONTENT_URI = "content://"
    const val EXTRA_FACULTY_DATA = "faculty data"
    const val FILE_URI = "file://"
    val TEACHER_POSTS = arrayOf(
        "Select Teacher Post",
        FacultyPosts.CHAIRMAN.values,
        FacultyPosts.PROFESSOR.values,
        FacultyPosts.ASSISTANT_PROFESSOR.values,
        FacultyPosts.ASSOCIATE_PROFESSOR.values,
        FacultyPosts.LECTURER.values,
        FacultyPosts.HONORARY_PROFESSOR.values,
    )
}