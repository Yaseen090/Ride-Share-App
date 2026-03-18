package com.map.helper;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.util.Log;
import com.map.GeoMapLoader;
import com.map.Polyline;
import com.map.models.LatLng;
import com.map.models.PolylineOptions;
import com.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class PolyLineAnimator {
    public static int NON_HIGHLIGHT_COLOR = Color.parseColor("#FFA7A6A6");
    public static int HIGHLIGHT_COLOR = Color.BLACK;

    private static PolyLineAnimator polyLineAnimator;
    private Polyline backgroundPolyline;
    private Polyline foregroundPolyline;
    private PolylineOptions optionsForeground;
    /*private AnimatorSet firstRunAnimSet;
    private AnimatorSet secondLoopRunAnimSet;*/
    Context mContext;
    List<LatLng> routePointList = null;
    GeoMapLoader.GeoMap geoMap = null;

    ValueAnimator polylineValueAnimator;

    int animCount = 0;

    private PolyLineAnimator() {

    }

    public static PolyLineAnimator getInstance() {
        if (polyLineAnimator == null) polyLineAnimator = new PolyLineAnimator();
        return polyLineAnimator;
    }

    public void stopRouteAnim() {
        Log.e("PolyLineP", "::StopRouteCalled::");
        try {
            /*if (firstRunAnimSet != null) {
                firstRunAnimSet.removeAllListeners();
                firstRunAnimSet.end();
                firstRunAnimSet.cancel();
            }
            if (secondLoopRunAnimSet != null) {
                secondLoopRunAnimSet.removeAllListeners();
                secondLoopRunAnimSet.end();
                secondLoopRunAnimSet.cancel();
            }*/

            if (polylineValueAnimator != null) {
                polylineValueAnimator.removeAllListeners();
                polylineValueAnimator.end();
                polylineValueAnimator.cancel();
                polylineValueAnimator = null;
            }

            if (backgroundPolyline != null) {
                backgroundPolyline.remove();
                backgroundPolyline = null;
            }
            if (foregroundPolyline != null) {
                foregroundPolyline.remove();
                foregroundPolyline = null;
            }

            if (routePointList != null) {
                routePointList.clear();
            }
            if (geoMap != null) {
                geoMap = null;
            }

            if (optionsForeground != null) {
                optionsForeground = null;
            }
            PolyLineAnimator.polyLineAnimator = null;
//            optionsForeground = new PolylineOptions().color(HIGHLIGHT_COLOR).width(Utils.dipToPixels(mContext, 5));
//            optionsForeground.addAll(routePointList);
//            foregroundPolyline = googleMap.addPolyline(optionsForeground);
        } catch (Exception e) {
            Log.e("PolyLineP", "::Exception::" + e.getMessage());
        }
    }

    private void resetPolyLines() {
        //Reset the polylines
        if (foregroundPolyline != null) {
            foregroundPolyline.remove();
            foregroundPolyline = null;
        }
        if (backgroundPolyline != null) {
            backgroundPolyline.remove();
            backgroundPolyline = null;
        }

        PolylineOptions optionsBackground = new PolylineOptions().color(NON_HIGHLIGHT_COLOR).width(5);
        backgroundPolyline = geoMap.addPolyline(optionsBackground);

        optionsForeground = new PolylineOptions().color(HIGHLIGHT_COLOR).width(5);
        foregroundPolyline = geoMap.addPolyline(optionsForeground);
    }

    public void animateRoute(GeoMapLoader.GeoMap geoMap, ArrayList<LatLng> routePointList, Context mContext) {
        this.mContext = mContext;
        this.routePointList = routePointList;
        this.geoMap = geoMap;

        Logger.e("PolyLineP", ":TotalPoints:" + routePointList.size());

        resetPolyLines();

        if (polylineValueAnimator != null) {
            polylineValueAnimator.removeAllListeners();
            polylineValueAnimator.end();
            polylineValueAnimator.cancel();
            polylineValueAnimator = null;
        }

        polylineValueAnimator = ValueAnimator.ofInt(0, 100);
        polylineValueAnimator.setDuration(3000);
        polylineValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        polylineValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        polylineValueAnimator.setInterpolator(new DecelerateInterpolator());
        polylineValueAnimator.addUpdateListener(animation -> {
            int percentValue = (int) animation.getAnimatedValue();
            int size = routePointList.size();
            int newPoints = (int) (size * (percentValue / 100.0f));
            ArrayList<LatLng> p = new ArrayList<>();
            p.addAll(routePointList.subList(0, newPoints));

//            Logger.e("PolyLineP", ":TMpSize:" + p.size());
            if (animCount % 2 == 0) {
                if (foregroundPolyline != null) {
                    foregroundPolyline.setPoints(p);
                }
            } else {
                if (backgroundPolyline != null) {
                    backgroundPolyline.setPoints(p);
                }
            }
        });

        polylineValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {

                Logger.e("PolyLineP", ":AnimStart:");
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                Logger.e("PolyLineP", ":AnimEnd:");
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {
                Logger.e("PolyLineP", ":AnimCancel:");
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {
                Logger.e("PolyLineP", ":AnimRepeat:");
                animCount++;

                if (animCount % 2 == 0) {
                    if (foregroundPolyline != null) {
                        foregroundPolyline.remove();
                        foregroundPolyline = null;
                    }

                    optionsForeground = new PolylineOptions().color(HIGHLIGHT_COLOR).width(5);
                    foregroundPolyline = geoMap.addPolyline(optionsForeground);

                } else {
                    if (backgroundPolyline != null) {
                        backgroundPolyline.remove();
                        backgroundPolyline = null;
                    }

                    PolylineOptions optionsBackground = new PolylineOptions().color(NON_HIGHLIGHT_COLOR).width(5);
                    backgroundPolyline = geoMap.addPolyline(optionsBackground);
                }

            }
        });
        polylineValueAnimator.start();

        /*if (firstRunAnimSet == null) {
            firstRunAnimSet = new AnimatorSet();
        } else {
            firstRunAnimSet.removeAllListeners();
            firstRunAnimSet.end();
            firstRunAnimSet.cancel();

            firstRunAnimSet = new AnimatorSet();
        }
        if (secondLoopRunAnimSet == null) {
            secondLoopRunAnimSet = new AnimatorSet();
        } else {
            secondLoopRunAnimSet.removeAllListeners();
            secondLoopRunAnimSet.end();
            secondLoopRunAnimSet.cancel();

            secondLoopRunAnimSet = new AnimatorSet();
        }
        //Reset the polylines
        if (foregroundPolyline != null) foregroundPolyline.remove();
        if (backgroundPolyline != null) backgroundPolyline.remove();


        PolylineOptions optionsBackground = new PolylineOptions().add(routePointList.get(0)).color(NON_HIGHLIGHT_COLOR).width(Utils.dipToPixels(mContext, 5));
        backgroundPolyline = geoMap.addPolyline(optionsBackground);

        optionsForeground = new PolylineOptions().add(routePointList.get(0)).color(HIGHLIGHT_COLOR).width(Utils.dipToPixels(mContext, 5));
        foregroundPolyline = geoMap.addPolyline(optionsForeground);

        final ValueAnimator percentageCompletion = ValueAnimator.ofInt(0, 100);
        percentageCompletion.setDuration(1800);
        percentageCompletion.setInterpolator(new DecelerateInterpolator());
        percentageCompletion.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (backgroundPolyline == null || foregroundPolyline == null) {
                    //   percentageCompletion.end();
                    //  percentageCompletion.cancel();
                    return;
                }
                ArrayList<LatLng> foregroundPoints = backgroundPolyline.getPoints();

                int percentageValue = (int) animation.getAnimatedValue();
                int pointcount = foregroundPoints.size();
                int countTobeRemoved = (int) (pointcount * (percentageValue / 100.0f));
                List<LatLng> subListTobeRemoved = foregroundPoints.subList(0, countTobeRemoved);
                subListTobeRemoved.clear();

                foregroundPolyline.setPoints(foregroundPoints);
            }
        });
        percentageCompletion.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (foregroundPolyline == null) {

                    return;
                }
                foregroundPolyline.setColor(NON_HIGHLIGHT_COLOR);
                foregroundPolyline.setPoints(backgroundPolyline.getPoints());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), NON_HIGHLIGHT_COLOR, HIGHLIGHT_COLOR);
        colorAnimation.setInterpolator(new AccelerateInterpolator());
        colorAnimation.setDuration(15); // milliseconds

        colorAnimation.addUpdateListener(animator -> {
            if (foregroundPolyline == null) {
                return;
            }
            foregroundPolyline.setColor((int) animator.getAnimatedValue());
        });

        if (routePointList == null || routePointList.size() == 0) {
            return;
        }

        ObjectAnimator foregroundRouteAnimator = ObjectAnimator.ofObject(this, "routeIncreaseForward", new RouteEvaluator(), routePointList.toArray());
        foregroundRouteAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        foregroundRouteAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (backgroundPolyline == null) {
                    return;
                }
                backgroundPolyline.setPoints(foregroundPolyline.getPoints());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        foregroundRouteAnimator.setDuration(2500);
//        foregroundRouteAnimator.start();

        firstRunAnimSet.playSequentially(foregroundRouteAnimator, percentageCompletion);
        firstRunAnimSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                secondLoopRunAnimSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        secondLoopRunAnimSet.playSequentially(colorAnimation, percentageCompletion);
        secondLoopRunAnimSet.setStartDelay(10);

        secondLoopRunAnimSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                secondLoopRunAnimSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        firstRunAnimSet.start();*/

    }

    /**
     * This will be invoked by the ObjectAnimator multiple times. Mostly every 16ms.
     **/
    public void setRouteIncreaseForward(LatLng endLatLng) {
        ArrayList<LatLng> foregroundPoints = foregroundPolyline.getPoints();
        foregroundPoints.add(endLatLng);
        foregroundPolyline.setPoints(foregroundPoints);
    }

    public class RouteEvaluator implements TypeEvaluator<LatLng> {
        @Override
        public LatLng evaluate(float t, LatLng startPoint, LatLng endPoint) {
            double lat = startPoint.latitude + t * (endPoint.latitude - startPoint.latitude);
            double lng = startPoint.longitude + t * (endPoint.longitude - startPoint.longitude);
            return new LatLng(lat, lng);
        }
    }
}
