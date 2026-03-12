import java.util.*;

class VideoData {
    String videoId;
    String content;

    VideoData(String id, String content) {
        this.videoId = id;
        this.content = content;
    }
}

public class CacheSystem {

    // L1 cache with LRU (10,000)
    LinkedHashMap<String, VideoData> L1;

    // L2 cache (100,000)
    HashMap<String, VideoData> L2 = new HashMap<>();

    // L3 database
    HashMap<String, VideoData> L3 = new HashMap<>();

    int l1Hits = 0;
    int l2Hits = 0;
    int dbHits = 0;

    public CacheSystem() {

        L1 = new LinkedHashMap<String, VideoData>(10000, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > 10000;
            }
        };
    }

    // Get video
    public VideoData getVideo(String videoId) {

        // L1 check
        if (L1.containsKey(videoId)) {
            l1Hits++;
            System.out.println("L1 Cache HIT");
            return L1.get(videoId);
        }

        // L2 check
        if (L2.containsKey(videoId)) {
            l2Hits++;
            System.out.println("L2 Cache HIT → Promoting to L1");

            VideoData v = L2.get(videoId);
            L1.put(videoId, v);
            return v;
        }

        // L3 database
        if (L3.containsKey(videoId)) {
            dbHits++;
            System.out.println("Database HIT → Promoting to L2");

            VideoData v = L3.get(videoId);
            L2.put(videoId, v);
            return v;
        }

        System.out.println("Video not found");
        return null;
    }

    // Add video to database
    public void addVideo(String id, String content) {
        L3.put(id, new VideoData(id, content));
    }

    // Show cache stats
    public void showStats() {

        System.out.println("L1 Hits: " + l1Hits);
        System.out.println("L2 Hits: " + l2Hits);
        System.out.println("DB Hits: " + dbHits);
    }

    public static void main(String[] args) {

        CacheSystem cache = new CacheSystem();

        cache.addVideo("video_123", "Movie A");
        cache.addVideo("video_456", "Movie B");

        cache.getVideo("video_123");
        cache.getVideo("video_123");
        cache.getVideo("video_456");

        cache.showStats();
    }
}