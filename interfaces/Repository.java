package interfaces;

import java.util.Optional;

public interface Repository<T> {
    Optional<T> findById(int id);

    void save(T entity);

    void update(T entity);

    void delete(int id);
}
