package com.ly.easysource.components.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;

/**
 * 原理：
 * 1 通过binder向其他组件甚至应用提供数据,通过AMS根据uri获取对应的provider的Binder接口IContentProvider，
 * 通过这个接口访问provider的数据源。
 * 2 provider可以用android:multiprocess指定多实例，每个调用进程都会有一个provider对象，避免进程间开销。
 * 但是实际缺乏使用场景。
 * 3 provider所在进程未启动时，第一次访问它会触发provider的创建，当然也伴随着provider进程的启动。
 *   和receiver一样，仅限6.0之前。
 * 4 所在应用访问时，里面的增删改查是在主线程；其他应用访问时，是在Binder线程。
 *
 * 好处：
 * 1 统一了数据访问方式,但其实如果数据源不是数据库，那么要自定义Cursor，非常麻烦。
 *
 * 使用：
 * 1 xml中声明android:authorities,这就是内容提供者的域名
 * 2 访问的时候通过构造Uri.parse("content://authorities")，只要scheme和域名匹配就可以访问到远程provider！！！
 *   后面的路径完全是自定义的：
 *   1）如果有多个数据集，可以指定路径Uri.parse("content://authorities/path"),如果内容是数据库，path就可以对应表名
 *   2）如果每个数据集有多条记录，可以Uri.parse("content://authorities/path/1")如果内容是数据库，1就可以对应第一条记录
 */
public class MyProvider extends ContentProvider{
    /**
     *这个onCreate先于Application的onCreate
     */
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
    /**
     * Binder object that deals with remoting.
     * ContentResolver获得的IContentProvider的实现binder是此类
     * @hide
     */
    class Transport extends ContentProviderNative {
        @Override
        public Cursor query(String callingPkg, Uri uri, String[] projection,
                            String selection, String[] selectionArgs, String sortOrder,
                            ICancellationSignal cancellationSignal) {
            return ContentProvider.this.query(
                    uri, projection, selection, selectionArgs, sortOrder,
                    CancellationSignal.fromTransport(cancellationSignal));
        }
        @Override
        public Uri insert(String callingPkg, Uri uri, ContentValues initialValues) {

            return maybeAddUserId(ContentProvider.this.insert(uri, initialValues), userId);

        }
        @Override
        public int delete(String callingPkg, Uri uri, String selection, String[] selectionArgs) {

            return ContentProvider.this.delete(uri, selection, selectionArgs);

        }
        @Override
        public int update(String callingPkg, Uri uri, ContentValues values, String selection,
                          String[] selectionArgs) {

            return ContentProvider.this.update(uri, values, selection, selectionArgs);

        }

    }
}
