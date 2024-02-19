package orlov641p.khai.edu.com.service;

import java.util.List;

public interface Service<T> {
    boolean add(T object);

    T getById(String id);

    List<T> findAll();
}