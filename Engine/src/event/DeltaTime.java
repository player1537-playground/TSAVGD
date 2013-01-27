package event;

import java.util.*;

class DeltaTime {
    static long oldTime;
    static HashMap<String, ArrayList<Long>> map = new HashMap<String, ArrayList<Long>>();
    static HashMap<String, Long> startTimeMap = new HashMap<String, Long>();
    
    public DeltaTime() {
	oldTime = EventTest.getTime();
    }

    public static void startFun(String name) {
	startTimeMap.put(name, new Long(EventTest.getTime()));
	oldTime = EventTest.getTime();
    }
    
    public static void timeFun(String name) {
	long deltaTime;
	if (startTimeMap.get(name) == null) {
	    deltaTime = EventTest.getTime() - startTimeMap.get(name).longValue();
	} else {
	    deltaTime = EventTest.getTime() - oldTime;
	}
	if (map.get(name) == null) {
	    map.put(name, new ArrayList<Long>());
	}
	map.get(name).add(new Long(deltaTime));
	oldTime = EventTest.getTime();
    }

    public static void printEverything() {
	System.out.println("Delta Times:");
	for (String key : map.keySet()) {
	    double averageDelta = DeltaTime.calcAverage(map.get(key));
	    System.out.println("   " + key + ":" + averageDelta);
	}
    }

    private static double calcAverage(ArrayList<Long> times) {
	int len = times.size();
	long total = 0L;
	for (Long l : times) {
	    total += l.longValue();
	}
	return (double)total / (double)len;
    }
}
