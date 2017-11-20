#   屏幕适配
+   所有标注的尺寸使用dp:@dimen/dp_1,R.dimen.dp_1
+   所有标注的字体大小使用sp:@dimen/sp_1,R.dimen.sp_1

#   图片加载
+   普通图片使用ImageView
+   大图或者需加载的图片使用ImgView（目前使用fresco，继承Simple）

#   键值对本地保存
+   根据键名在AppKeyStorage创建一对读写方法
+   AppKeyStorage -->  KeyStorage  -->  SharedPreferences