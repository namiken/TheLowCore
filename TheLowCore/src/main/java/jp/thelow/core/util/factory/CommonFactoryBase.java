package jp.thelow.core.util.factory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class CommonFactoryBase<K, T> {

  /** 準備アクションのMap */
  protected Map<K, T> entityInstanceMap = new HashMap<>();

  /**
   * エンティティを登録する。
   *
   * @param entity エンティティ
   * @return TODO
   */
  public T register(T entity) {
    return entityInstanceMap.put(getId(entity), entity);
  }

  /**
   * IDからエンティティを取得する。
   *
   * @param id エンティティID
   * @return エンティティ
   */
  public T findById(K id) {
    return entityInstanceMap.get(id);
  }

  /**
   * エンティティからエンティティIDを取得する。
   *
   * @param entity エンティティ
   * @return エンティティID
   */
  protected abstract K getId(T entity);

  /**
   * すべての登録情報を削除する。
   */
  public void clear() {
    entityInstanceMap.clear();
  }

  /**
   * 登録済みのエンティティIDリストを取得する。
   *
   * @return 登録済みのエンティティIDリスト
   */
  public Set<K> getStoredIdList() {
    return entityInstanceMap.keySet();
  }

  /**
   * 登録済みのエンティティのリストを取得する。
   *
   * @return 登録済みのエンティティのリスト
   */
  public Collection<T> getStoredEntityList() {
    return entityInstanceMap.values();
  }

  /**
   * 指定したエンティティIDに紐づくエンティティが存在する場
   *
   * @param id エンティティID
   * @return 存在すればID
   */
  public boolean contains(K id) {
    return entityInstanceMap.containsKey(id);
  }
}
