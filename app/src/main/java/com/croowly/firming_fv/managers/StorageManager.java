package com.croowly.firming_fv.managers;

import android.content.Context;
import android.content.res.AssetManager;

import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storable;
import com.sromku.simple.storage.Storage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by CHARS on 3/15/2017.
 */

public class StorageManager {

    private static Storage storage = null;

    public static boolean initStorage(Context context) {

        try {
            if (SimpleStorage.isExternalStorageWritable()) {
                storage = SimpleStorage.getExternalStorage();

            } else {
                storage = SimpleStorage.getInternalStorage(context);
            }
            return true;
        } catch (Exception e) {
           // Log.e(getClass().getName(), App.getContext().getString(R.string.error_storage) + e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    public static boolean createDir(String dirname, boolean override) {
        try {
            return storage.createDirectory(dirname, override);
        } catch (Exception e) {
           // Log.e(getClass().getName(), App.getContext().getString(R.string.error_creating_directory) + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public static  boolean createFile(String dirname, String filename, String file) {
        try {
            return storage.createFile(dirname, filename, file);
        } catch (Exception e) {
            // Log.e(getClass().getName(), App.getContext().getString(R.string.error_creating_file) + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void appendFile(String dirname, String filename, String file) {
        try {
            storage.appendFile(dirname, filename, file);
        } catch (Exception e) {
            // Log.e(getClass().getName(), App.getContext().getString(R.string.error_creating_file) + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    public static  boolean createFile(String dirname, String filename, Storable fileContent) {
        try {
            return storage.createFile(dirname, filename, fileContent);
        } catch (Exception e) {
            // Log.e(getClass().getName(), App.getContext().getString(R.string.error_creating_file) + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public static boolean createFile(String dirname, String filename, byte[] fileContent) {
        try {
            return storage.createFile(dirname, filename, fileContent);
        } catch (Exception e) {
            // Log.e(getClass().getName(), App.getContext().getString(R.string.error_creating_file) + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean appendToFile(String dirname, String filename, String moreContent) {
        try {
            storage.appendFile(dirname, filename, moreContent);
        } catch (Exception e) {
            // Log.e(getClass().getName(), App.getContext().getString(R.string.error_creating_file) + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static File getFile(String dirname, String filename) {
        try {
            return storage.getFile(dirname, filename);
        } catch (Exception e) {
            // Log.e(getClass().getName(), App.getContext().getString(R.string.error_reading_file) + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] readFile(String dirname, String filename) {
        try {
            return storage.readFile(dirname, filename);
        } catch (Exception e) {
            // Log.e(getClass().getName(), App.getContext().getString(R.string.error_reading_file) + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public static void copyFile(File file, String dirname, String newfilename) {
        try {
            storage.copy(file,dirname,newfilename);
        } catch (Exception e) {
            // Log.e(getClass().getName(), App.getContext().getString(R.string.error_storage) + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void moveFile(File file, String dirname, String newfilename) {
        try {
            storage.move(file, dirname, newfilename);
        } catch (Exception e) {
            // Log.e(getClass().getName(), App.getContext().getString(R.string.error_storage) + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean delDirectory(String dirname) {
        try {
            return storage.deleteDirectory(dirname);
        } catch (Exception e) {
            // Log.e(getClass().getName(), App.getContext().getString(R.string.error_deleting_directory) + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delFile(String dirname, String filename) {
        try {
            return storage.deleteFile(dirname, filename);
        } catch (Exception e) {
            // Log.e(getClass().getName(), App.getContext().getString(R.string.error_deleting_file) + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<File> getFiles(String dirname, String regex){
        try {
            return storage.getFiles(dirname, regex);
        } catch (Exception e) {
            // Log.e(getClass().getName(), App.getContext().getString(R.string.error_reading_file) + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean directoryExists(String dirname) {

        return storage.isDirectoryExists(dirname);
    }

    public static boolean fileExists(String dirname, String filename) {

        return storage.isFileExist(dirname,filename);
    }


    public static String formatFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/ Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/ Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


     public static void copyFilesFromAssets(Context context, String dirname, String ext) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            // Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            if(filename.contains(ext)) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open(filename);
                    createFile(dirname,filename,toByteArray(in));


                } catch (Exception e) {
                    // Log.e("tag", "Failed to copy asset file: " + filename, e);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                }
            }
        }
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024];
        while (read != -1) {
            read = in.read(buffer);
            if (read != -1)
                out.write(buffer,0,read);
        }
        out.close();
        return out.toByteArray();
    }
}
