# SmartSpinner
特定spinner定制、智能弹出listview或gridview

NiceSpinner is a re-implementation of the default Android's spinner, with a nice arrow animation and a different way to display its content.

It follows the material design guidelines, and it is compatible starting from Api 14.

## Usage

The usage is pretty straightforward. Add the tag into the XML layout:
   ``` <com.boylab.smartspinner.SmartSpinner
	    android:id="@+id/smart_Spinner01"
	    android:layout_width="wrap_content"
	    android:layout_height="wrap_content"
	    android:layout_centerHorizontal="true"
	    android:layout_gravity="center"
	    android:layout_marginTop="40dp"
	    android:textSize="20sp"
	    app:arrowDrawable="@drawable/ds15_arrow"
	    app:arrowTint="@android:color/darker_gray"
	    app:itemHeight="50dp"
	    app:itemWidth="160dp"
	    app:numColumns="1" />
```


* Note: change `layout_width` to at least the width of the largest item on the list to prevent resizing

 Then use this snippet to populate it with contents:
```java
 	smartSpinner01 = findViewById(R.id.smart_Spinner01);
	List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five", "Six", "Seven"));
	smartSpinner01.attachDataSource(dataset);
```

#### Listeners
For listening to the item selection actions, you can just use the following snippet:
```java
smartSpinner01.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
    @Override
    public void onItemSelected(SmartSpinner parent, View view, int position, long id) {
        // This example uses String, but your type can be any
        String item = parent.getItemAtPosition(position);
        ...
    }
});
```

#### Attributes
You can add attributes to customize the view. Available attributes:

| name                      | type      | info                                                   |
|------------------------   |-----------|--------------------------------------------------------|
| arrowTint                 | color     | sets the color on the drop-down arrow                  |
| hideArrow                 | boolean   | set whether show or hide the drop-down arrow           |
| arrowDrawable             | reference | set the drawable of the drop-down arrow                |
| textTint                  | color     | set the text color                                     |
| dropDownListPaddingBottom | dimension | set the bottom padding of the drop-down list           |
| backgroundSelector        | integer   | set the background selector for the drop-down list rows |
| popupTextAlignment        | enum      | set the horizontal alignment of the default popup text |
| entries                   | reference | set the data source from an array of strings |

How to include
---

With gradle: edit your `build.gradle`:
```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}

dependencies {

    implementation 'com.github.boylab:SmartSpinner:1.0.0'
}
```

Or declare it into your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.boylab</groupId>
    <artifactId>SmartSpinner</artifactId>
    <version>1.0.0</version>
</dependency>
```
