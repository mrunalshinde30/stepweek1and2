import java.util.HashMap;

class TokenBucket {

    int tokens;
    int maxTokens;
    long lastRefillTime;

    TokenBucket(int maxTokens) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // refill tokens every hour
    void refill() {

        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime;

        if (elapsed >= 3600000) { // 1 hour = 3600000 ms
            tokens = maxTokens;
            lastRefillTime = now;
        }
    }
}

public class RateLimiter {

    HashMap<String, TokenBucket> clients = new HashMap<>();
    int LIMIT = 1000;

    public String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(LIMIT));

        TokenBucket bucket = clients.get(clientId);

        bucket.refill();

        if (bucket.tokens > 0) {
            bucket.tokens--;
            return "Allowed (" + bucket.tokens + " requests remaining)";
        }

        long retry = 3600 - (System.currentTimeMillis() - bucket.lastRefillTime) / 1000;

        return "Denied (0 requests remaining, retry after " + retry + "s)";
    }

    public String getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        int used = bucket.maxTokens - bucket.tokens;

        return "{used: " + used + ", limit: " + bucket.maxTokens + "}";
    }

    public static void main(String[] args) {

        RateLimiter rl = new RateLimiter();

        System.out.println(rl.checkRateLimit("abc123"));
        System.out.println(rl.checkRateLimit("abc123"));
        System.out.println(rl.getRateLimitStatus("abc123"));
    }
}