package cn.czyugang.tcg.client.utils.img;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import cn.czyugang.tcg.client.utils.storage.FileStorage;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        //获取内存的默认配置
//        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
//        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
//        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
//        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
//        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);
//        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
//        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        //内存缓存相关,默认是24m
        builder.setMemoryCache(new LruResourceCache(1024 * 1024 * 20));

        //设置磁盘缓存及其路径
        builder.setDiskCache(new DiskLruCacheFactory(FileStorage.getImgCacheDir(), 1024 * 1024 * 100));

    }


    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        //配置glide网络加载框架
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

    @Override
    public boolean isManifestParsingEnabled() {
        //不使用清单配置的方式,减少初始化时间
        return false;
    }
}
