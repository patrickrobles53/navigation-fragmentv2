package admin4.techelm.com.techelmtechnologies.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin 4 on 22/02/2017.
 * Credit to Ilya Gazman on 3/6/2016.
 * SAVE FILE to Directory specified
 * MODE of file EXTERNAL or INTERNAL(PRIVATE)
 * USAGE:
     * To save:
         new ImageSaver(context).
         setFileName("myImage.png").
         setDirectoryName("images").
         save(bitmap);
     * To load:
         Bitmap bitmap = new ImageSaver(context).
         setFileName("myImage.png").
         setDirectoryName("images").
         load();
 *      ImageSaver.setExternal(boolean)
 */

public class FileUtility {

    /**
     * Created
     */

    private String directoryName = "images";
    private String fileName = "image.png";
    private Context context;
    private boolean external;
    private File file = null;

    public FileUtility(Context context) {
        this.context = context;
    }

    public FileUtility setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public FileUtility setExternal(boolean external) {
        this.external = external;
        return this;
    }

    public FileUtility setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
        return this;
    }

    public File getFile() {
        return this.file;
    }

    public String getFileName() {
        return this.fileName;
    }

    private void setFile(File file) {
        this.file = file;
    }

    public boolean deleteFile() {
        File file = createFile();
        return file.delete();
    }

    public boolean saveImage(Bitmap bitmapImage) {
        boolean result = false;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile());
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    @NonNull
    private File createFile() {
        File directory;
        if (external) {
            directory = getAlbumStorageDir(directoryName);
        } else {
            directory = context.getDir(directoryName, Context.MODE_PRIVATE);
        }
        setFile(directory); // Just Set the file
        return new File(directory, fileName);
    }

    private File getAlbumStorageDir(String albumName) {
        /* File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName); */
        String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFilePath += "/TELCHEM/" + albumName;
        File file = new File(mFilePath);
        if (!file.mkdirs()) {
            Log.e("ImageUtility", "Directory not created, or already existed");
        }
        return file;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public Bitmap loadImage() {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(createFile());
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
