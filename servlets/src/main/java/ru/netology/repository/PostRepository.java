package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// Stub
public class PostRepository {

    private ConcurrentMap<Long, Post> rep;
    private AtomicLong counter = new AtomicLong(0L);

    public PostRepository() {
        rep = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        return new ArrayList<>(rep.values());
    }

    // если post с таким id не найден, то возвращаем пустой элемент
    public Optional<Post> getById(long id) {
        if (!rep.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(rep.get(id));
    }

    // если post с таким id !=0 не найден, то добавляем этот post в коллекцию c id, равному следующему
    // по порядку в коллекции, а иначе у нас будут пересечения, что не очень
    // если post с таким id <= максимальному счетчику элемента в коллекции, то мы либо обновляем
    // существующий элемент, либо вставляем новый в незанятую ячейку
    public Post save(Post post) {
        if (post.getId() <= counter.get() && post.getId() != 0) {
            rep.put(post.getId(), post);
        } else {
            post.setId(counter.incrementAndGet());
            rep.put(post.getId(), post);
        }
        return post;
    }

    // если post с таким id не найден, то возвращаем пустой элемент, иначе возвращаем удаляемый post
    public Optional<Post> removeById(long id) {
        if (!rep.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(rep.remove(id));
    }
}
