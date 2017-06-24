package restaurant.model.dao;

import java.util.List;

/**
 *
 * @author igor
 */
public interface DAO<T> {
    boolean insert(T object);
    boolean update(T object);
    boolean delete(int id);
    T read(int id);
    List<T> readAll();
}
