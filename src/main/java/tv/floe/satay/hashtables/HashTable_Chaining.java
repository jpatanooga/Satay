package tv.floe.satay.hashtables;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 
 * - hash value of a data item x, denoted by hash(x)
 * -- is in the range of { 0, ..., arr.length-1 }
 * 
 * - all items with hash value i are stored in the list at arr[i] 
 * 
 * 
 * Map ---------------------------------------------------------------
 * - interface
 * - An object that maps keys to values. 
 * - A map cannot contain duplicate keys; 
 * - each key can map to at most one value.
 * 
 * 
 *
 */
public class HashTable_Chaining<K,V>
              implements Map<K,V>, Cloneable, java.io.Serializable {

  // hash table data
  private transient Entry[] table;
  
  // number of entries in hashtable
  private transient int count;
  
  // threshold for re-hashing
  private int threshold;
  
  // load factor for hashtable
  private float loadFactor;
  
  // number of times the hashtable has been structurally modified
  private transient int modCount = 0;
  
  // max rehash array size
  private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
  
  public HashTable_Chaining(int capacity, float loadFactor) {
    
    if (capacity < 0) {
      throw new IllegalArgumentException( "Illegal Capacity: " + capacity );
    }
    
    if (loadFactor <= 0 || Float.isNaN(loadFactor) ) {
      
      throw new IllegalArgumentException( "Illegal Load: " + loadFactor );
      
    }
    
    if (capacity == 0) {
      capacity = 1;
    }
    
    this.loadFactor = loadFactor;
    this.table = new Entry[capacity];
    threshold = (int)(capacity * loadFactor);
    
  }
  
  // init capacity, default load factor
  
  public HashTable_Chaining(int capacity) {
    this(capacity, 0.75f);
  }
  
  public HashTable_Chaining() {
    this(11, 0.75f);
  } 
  
  public synchronized int size() {
    return count;
  }
  
  public synchronized boolean isEmpty() {
    return count == 0;
  }
  
  public synchronized boolean contains( Object  val ) {
    
    if ( val == null ) {
      throw new NullPointerException();
    }
    
    Entry tab[] = table;
    for ( int i = tab.length; i-- > 0; ) {
      
      //for ( Entry<K,V> e = tab[i] ; e != null ; e = e. )
      return true;
      //}
    }
    
    return false;
  }
  
  public synchronized boolean containsKey(Object key) {
    
    Entry tab[] = table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    
    for (Entry<K,V> e = tab[index] ; e != null ; e = e.next) {
      if ((e.hash == hash) && e.key.equals(key)) {
          return true;
      }
    }
    
    return false;   
    
  }
  
  public synchronized V get(Object key) {
    
        Entry tab[] = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        
        for (Entry<K,V> e = tab[index] ; e != null ; e = e.next) {
          if ((e.hash == hash) && e.key.equals(key)) {
            return e.value;
          }
        }
        
        return null;    
    
  }
  
  public synchronized V put(K key, V value) {
    
        // Make sure the value is not null
      if (value == null) {
        
        throw new NullPointerException();
        
      }
         
      // Makes sure the key is not already in the hashtable.
      Entry tab[] = table;
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      
      for (Entry<K,V> e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && e.key.equals(key)) {
          V old = e.value;
          e.value = value;
          return old;
        }
      }
         
      modCount++;

      if (count >= threshold) {
        // Rehash the table if the threshold is exceeded
        rehash();
           
        tab = table;
        index = (hash & 0x7FFFFFFF) % tab.length;
      }
         
      // Creates the new entry.
      Entry<K,V> e = tab[index];
      tab[index] = new Entry<K,V>(hash, key, value, e);
      count++;
      return null;    
    
  } 
  
  public synchronized V remove(Object key) {
    
        Entry tab[] = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        
        for (Entry<K,V> e = tab[index], prev = null ; e != null ; prev = e, e = e.next) {
          
          if ((e.hash == hash) && e.key.equals(key)) {
            
            modCount++;
            
            if (prev != null) {
              prev.next = e.next;
            } else {
              tab[index] = e.next;
            }
            
            count--;
            V oldValue = e.value;
            e.value = null;
            return oldValue;
          }
          
        }
        
        return null;    
    
  } 
  
  public synchronized Object clone() {
    
        try {

          HashTable_Chaining<K,V> t = (HashTable_Chaining<K,V>) super.clone();
      t.table = new Entry[table.length];
      
      for (int i = table.length ; i-- > 0 ; ) {
        t.table[i] = (table[i] != null) ? (Entry<K,V>) table[i].clone() : null;
      }
      
      t.modCount = 0;
      return t;

        } catch (CloneNotSupportedException e) {
          // this shouldn't happen, since we are Cloneable
          throw new InternalError();
        }   
    
  }
  
  /*
  * Increases the capacity of and internally reorganizes this
  * hashtable, in order to accommodate and access its entries more
  * efficiently.  This method is called automatically when the
  * number of keys in the hashtable exceeds this hashtable's capacity
  * and load factor.
  */
  protected void rehash() {
    int oldCapacity = table.length;
    Entry[] oldMap = table;
       
    // overflow-conscious code
    int newCapacity = (oldCapacity << 1) + 1;
    
    if (newCapacity - MAX_ARRAY_SIZE > 0) {
      
      if (oldCapacity == MAX_ARRAY_SIZE) {
        // Keep running with MAX_ARRAY_SIZE buckets
        return;
      }
      
      newCapacity = MAX_ARRAY_SIZE;
    }
    
    Entry[] newMap = new Entry[newCapacity];
       
    modCount++;
    threshold = (int)(newCapacity * loadFactor);
    table = newMap;
       
    for (int i = oldCapacity ; i-- > 0 ;) {
      for (Entry<K,V> old = oldMap[i] ; old != null ; ) {
        Entry<K,V> e = old;
        old = old.next;
        
        int index = (e.hash & 0x7FFFFFFF) % newCapacity;
        e.next = newMap[index];
        newMap[index] = e;
      }
    }
  }
      
  
  
  private static class Entry<K,V> implements Map.Entry<K,V> {
    int hash;
    K key;
    V value;
    Entry<K,V> next;
    
    protected Entry(int hash, K key, V value, Entry<K,V> next) {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.next = next;
    }
    
    protected Object clone() {
      return new Entry<K,V>(hash, key, value, (next == null ? null : (Entry<K,V>) next.clone() ) );
    }
    
    public K getKey() {
      return key;
    }
    
  
    
    public boolean equals(Object o) {
      if (!(o instanceof Map.Entry))
          return false;
          
      Map.Entry e = (Map.Entry)o;
    
      return (key==null ? e.getKey()==null : key.equals(e.getKey())) && (value==null ? e.getValue()==null : value.equals(e.getValue()));      
    }
    
    public int hashCode() {
      return hash ^ (value==null ? 0 : value.hashCode());     
    }
    
    public String toString() {
      return key.toString()+"="+value.toString();     
    }

    @Override
    public V getValue() {
      return value;
    }

    @Override
    public V setValue(V value) {
     if (value == null)
        throw new NullPointerException();
             
      V oldValue = this.value;
      this.value = value;
      return oldValue;
    }
  }
  
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

  @Override
  public void clear() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean containsValue(Object arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Set<java.util.Map.Entry<K, V>> entrySet() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<K> keySet() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Collection<V> values() {
    // TODO Auto-generated method stub
    return null;
  }

}