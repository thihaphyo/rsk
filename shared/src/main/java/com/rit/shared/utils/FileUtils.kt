package com.rit.shared.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import android.webkit.MimeTypeMap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Locale
import org.apache.commons.io.IOUtils

object FileUtils {

    fun toBase64(file: File): String? {
        return try {
            Base64.encodeToString(getBytes(file.inputStream()), Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    fun getFile(context: Context, uri: Uri): File {
        val fileName =
            getFileNameFromUri(uri, context)
        val fileType = getFileTypeString(fileName)
        val parcelFileDescriptor =
            context.contentResolver.openFileDescriptor(uri, "r", null)
        var file: File? = null
        parcelFileDescriptor?.let {
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            file = File(
                context.cacheDir,
                context.contentResolver.getFileName(uri)
            )
            val outputStream = FileOutputStream(file)
            IOUtils.copy(inputStream, outputStream)
        }
//        if (allowedImageTypes.contains(fileType.toLowerCase(Locale.US))) {
//            viewModel.uploadDepositAttachment(file, uri.toString())
//        }
        return file!!
    }

    fun getMimeType(file: File): String {
        val extension = file.path.split(".").lastOrNull() ?: ""
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: ""
    }

    @SuppressLint("Range")
    fun getFileNameFromUri(uri: Uri, context: Context): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
            cursor.close()
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    fun getFileType(uri: Uri, context: Context): FileType {
        val fileName = getFileNameFromUri(uri, context)
        return getFileType(fileName)
    }

    fun getFileType(file: File): FileType {
        return getFileType(file.name)
    }

    fun getFileType(fileName: String): FileType {
        val fileType = getFileTypeString(fileName)
        return if (fileType == "pdf") FileType.PDF else FileType.IMAGE
    }

    fun getFileTypeString(fileName: String): String {
        val extension =
            getExtensionFromFilename(fileName)
        return when {
            extension.lowercase(Locale.getDefault()) == "pdf" -> "pdf"
            extension.lowercase(Locale.getDefault()) == "jpg" -> "jpg"
            extension.lowercase(Locale.getDefault()) == "png" -> "png"
            extension.lowercase(Locale.getDefault()) == "jpeg" -> "jpeg"
            else -> {
                extension
            }
        }
    }

    private fun getExtensionFromFilename(fileName: String): String {
        return fileName.substring(fileName.lastIndexOf(".") + 1)
    }

    fun List<File>.sizeInMB(): Double {
        return if (this.isNotEmpty()) {
            return this.map { it.sizeInMB() }.reduce { acc, l ->
                acc + l
            }
        } else {
            0.0
        }
    }

    fun File.sizeInMB() = byte2MegaByte(length())

    private fun byte2MegaByte(sizeInBytes: Long): Double = sizeInBytes.toDouble() / 1024 / 1024
}

fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}

enum class FileType {
    PDF, IMAGE
}
