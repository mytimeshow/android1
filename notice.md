#   屏幕适配
+   所有标注的尺寸使用dp:@dimen/dp_1,R.dimen.dp_1
+   所有标注的字体大小使用sp:@dimen/sp_1,R.dimen.sp_1

#   图片加载
+   普通图片使用ImageView
+   大图或者需加载的图片使用ImgView.id(图片id)（目前使用fresco，继承SimpleDraweeView ）

#   键值对本地保存
+   根据键名在AppKeyStorage创建一对读写方法
+   AppKeyStorage -->  KeyStorage  -->  SharedPreferences

#   网络请求
+   登录状态 UserOAuth.judgeOrLogin()  未登录将跳转至登录界面

#   网络
+   接口全部写在api包
+   接收器使用BaseActivity.NetObserver
+   在onNext里面解析数据，先调用 if(!ErrorHandler.judge200(response)) return;