package hash;

public class ConcreteHashGuava implements HashStrategy {

    /**
     * This method was written by Doug Lea with assistance from members of JCP
     * JSR-166 Expert Group and released to the public domain, as explained at
     * http://creativecommons.org/licenses/publicdomain
     * <p>
     * As of 2010/06/11, this method is identical to the (package private) main.hash
     * method in OpenJDK 7's java.util.HashMap class.
     */
    public int hash(int hashCode) {
        hashCode ^= (hashCode >>> 20) ^ (hashCode >>> 12);
        return hashCode ^ (hashCode >>> 7) ^ (hashCode >>> 4);
    }
}
