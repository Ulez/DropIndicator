# DropIndicator
## gif
<img src="https://github.com/Ulez/DropIndicator/blob/master/screenshots/view.gif" width = "300" height = "507.6" align=center />

##How to use?
#####Step 1
```java
compile 'com.github.ulez:dropindicator:0.0.2'
```

#####Step 2
```xml
    <comulez.github.droplibrary.DropIndicator
        android:background="@color/colorPrimary"
        android:id="@+id/circleIndicator"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:layout_alignParentBottom="true"
        app:circle_color="@android:color/darker_gray"
        app:click_color="#fafafa"
        app:color1="#FCC04D"
        app:color2="#00C3E2"
        app:color3="#FE626D"
        app:color4="#966ACF"
        app:duration="800"
        app:radius="14dp"
        app:scale="0.5">
        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/msg" />
            ........
    </comulez.github.droplibrary.DropIndicator>
```
[Blog博客](http://blog.csdn.net/s122ktyt/article/details/55798658) 
## Lisence:

Lisenced under [Apache 2.0 lisence](http://opensource.org/licenses/Apache-2.0)
