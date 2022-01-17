package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {

    private Queue<Post> rep;
    private AtomicLong counter = new AtomicLong(0L);

    public PostRepository() {
        rep = new ConcurrentLinkedQueue<>();
    }

    public List<Post> all() {
        return new ArrayList<>(rep);
    }

    // если post с таким id не найден, то возвращаем пустой элемент
    public Optional<Post> getById(long id) {
        if (!rep.contains(new Post(id))) {
            return Optional.empty();
        }
        return rep.stream().filter(x -> x.getId() == id).findFirst();
    }

    private void addPost(Post post) {
        post.setId(counter.incrementAndGet());
        rep.add(post);
    }

    // если post с таким id !=0 не найден, то добавляем этот post в коллекцию c id, равному следующему
    // по порядку в коллекции, а иначе у нас будут пересечения, что не очень
    public Post save(Post post) {
        if (post.getId() != 0) {
            if (rep.contains(post)) {
                rep.remove(post);
                rep.add(post);
            } else {
                addPost(post);
            }
        } else {
            addPost(post);
        }
        return post;
    }

    // если post с таким id не найден, то возвращаем пустой элемент, иначе возвращаем удаляемый post
    public Optional<Post> removeById(long id) {
        if (!rep.contains(new Post(id))) {
            return Optional.empty();
        }
        final var post = rep.stream().filter(x -> x.getId() == id).findFirst();
        rep.remove(post.get());
        return post;
    }
}
