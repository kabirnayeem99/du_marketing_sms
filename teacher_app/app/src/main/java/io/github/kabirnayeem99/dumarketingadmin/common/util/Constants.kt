package io.github.kabirnayeem99.dumarketingadmin.common.util

import io.github.kabirnayeem99.dumarketingadmin.common.util.enum.ImageCats
import io.github.kabirnayeem99.dumarketingadmin.common.util.enum.TeacherPosts

object Constants {
    const val ROUTINE_DB_REF: String = "Routine"
    const val EBOOK_STORAGE_PATH: String = "Ebook"
    const val FACULTY_STORAGE_PATH: String = "Faculty"
    const val EBOOK_DB_REF: String = "Ebook"
    const val ABOUT_DB_REF: String = "About"
    const val FACULTY_DB_COLLECTION_NAME: String = "Faculty"
    const val NOTICE_IMAGE_PATH_STRING_FOLDER_NAME = "Notice"
    const val GALLERY_IMAGE_PATH_STRING_FOLDER_NAME = "Gallery"
    const val NOTICE_DB_REF = "Notice"
    const val GALLERY_DB_REF = "Gallery"
    const val CONTENT_URI = "content://"
    const val EXTRA_FACULTY_DATA = "faculty data"
    const val FILE_URI = "file://"

    const val IMAGE_QUALITY_LESSEN_PERCENT = 50

    val TEACHER_POSTS = arrayOf(
        "Select Teacher Post",
        TeacherPosts.CHAIRMAN.values,
        TeacherPosts.PROFESSOR.values,
        TeacherPosts.ASSISTANT_PROFESSOR.values,
        TeacherPosts.ASSOCIATE_PROFESSOR.values,
        TeacherPosts.LECTURER.values,
        TeacherPosts.HONORARY_PROFESSOR.values,
    )

    val IMAGE_CATS = arrayOf(
        ImageCats.CLASS.values,
        ImageCats.CONVOCATION.values,
        ImageCats.CLUB.values,
        ImageCats.OTHER.values,
    )
}