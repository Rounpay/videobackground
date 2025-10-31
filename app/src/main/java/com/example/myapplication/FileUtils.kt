package com.example.myapplication
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtils {

    fun getFilePathFromUri(context: Context, uri: Uri, uniqueName: Boolean): String {
        return if (uri.path?.contains("file://") == true) {
            uri.path!!
        } else {
            getFileFromContentUri(context, uri, uniqueName).path
        }
    }

    private fun getFileFromContentUri(context: Context, contentUri: Uri, uniqueName: Boolean): File {
        val fileExtension = getFileExtension(context, contentUri)
        val fileName = if (uniqueName) {
            "temp_${System.currentTimeMillis()}.$fileExtension"
        } else {
            queryName(context.contentResolver, contentUri)
        }

        val file = File(context.cacheDir, fileName)
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(contentUri)
            val outputStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var read: Int
            while (inputStream!!.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()
            outputStream.close()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }

    private fun queryName(resolver: ContentResolver, uri: Uri): String {
        val returnCursor: Cursor = resolver.query(uri, null, null, null, null)!!
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }

    private fun getFileExtension(context: Context, uri: Uri): String {
        var extension: String? = null
        extension = context.contentResolver.getType(uri)?.substringAfterLast('/')
        return extension ?: "jpg"
    }
}
