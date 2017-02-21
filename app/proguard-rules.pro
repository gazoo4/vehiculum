-keep class com.github.mikephil.charting.** { *; }
-dontwarn io.realm.**

# simpleframework.org broken dependencies
-dontwarn javax.xml.stream.**

# org.apache.commons.math3 broken dependencies
-dontwarn java.awt.geom.AffineTransform

# Optimizations
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
