# 自定义控件

* Android自身带的控件不能满足需求, 需要根据自己的需求定义控件.

## 自定义控件可以分为三大类型

### 1. 组合已有的控件实现
* Button或ImageButton等自带按钮功能的控件会抢夺所在Layout的焦点.导致其他区域点击不生效.在所在layout声明一个属性
   	 	android:descendantFocusability="blocksDescendants"
	    （你占多大区域，就在该区域内获取焦点就可以了）
* popupwindow获取焦点, 外部可点击
			// 设置点击外部区域, 自动隐藏
		popupWindow.setOutsideTouchable(true); // 外部可触摸
		popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 设置空的背景, 响应点击事件
		popupWindow.setFocusable(true); //设置可获取焦点

### 2. 完全自定义控件.(继承View, ViewGroup)

* 1. 自定义开关  
> - 1. 写个类继承View, OK
> - 2. 拷贝包含包名的全路径到xml中, OK
> - 3. 界面中找到该控件, 设置初始信息, OK
> - 4. 根据需求绘制界面内容,OK
> - 5. 响应用户的触摸事件,OK
> - 6. 创建一个状态更新监听.OK
	
	// 1. 声明接口对象
	public interface OnSwitchStateUpdateListener{
		// 状态回调, 把当前状态传出去
		void onStateUpdate(boolean state);
	}
	// 2. 添加设置接口对象的方法, 外部进行调用
	public void setOnSwitchStateUpdateListener(
			OnSwitchStateUpdateListener onSwitchStateUpdateListener) {
				this.onSwitchStateUpdateListener = onSwitchStateUpdateListener;
	}
	// 3. 在合适的位置.执行接口的方法	
	onSwitchStateUpdateListener.onStateUpdate(state);

	// 4. 界面/外部, 收到事件.
> - 7. 自定义属性

	1. 在attrs.xml声明节点declare-styleable

		<declare-styleable name="ToggleView">
	        <attr name="switch_background" format="reference" />
	        <attr name="slide_button" format="reference" />
	        <attr name="switch_state" format="boolean" />
	    </declare-styleable>

	2. R会自动创建变量

		attr 3个变量
		styleable 一个int数组, 3个变量(保存位置)

	3. 在xml配置声明的属性/ 注意添加命名空间
	
	    xmlns:itheima="http://schemas.android.com/apk/res/com.itheima74.toggleview"
		
        itheima:switch_background="@drawable/switch_background"
        itheima:slide_button="@drawable/slide_button"
        itheima:switch_state="false"

	4. 在构造函数中获取并使用

		// 获取配置的自定义属性
		String namespace = "http://schemas.android.com/apk/res/com.itheima74.toggleview";
		int switchBackgroundResource = attrs.getAttributeResourceValue(namespace , "switch_background", -1);
		

> - Android 的界面绘制流程
	 
	  测量			 摆放		绘制
	  measure	->	layout	->	draw
	  	  | 		  |			 |
	  onMeasure -> onLayout -> onDraw 重写这些方法, 实现自定义控件
	  
	  都在onResume()之后执行
	  
	  View流程
	  onMeasure() (在这个方法里指定自己的宽高) -> onDraw() (绘制自己的内容)
	  
	  ViewGroup流程
	  onMeasure() (指定自己的宽高, 所有子View的宽高)-> onLayout() (摆放所有子View) -> onDraw() (绘制内容)

	
### 3. 继承已有的控件实现(扩展已有的功能)
