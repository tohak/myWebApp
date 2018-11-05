package com.konovalov.web.service.common;

import com.konovalov.web.domain.common.BaseEntity;
import com.konovalov.web.repository.common.BaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Service
public abstract class AbstractBaseService<E extends BaseEntity> implements BaseService<E> {
    //получение название типа класса и записываем в строку
    private final String entityName = ((ParameterizedType) getClass()
            .getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();

    abstract BaseRepository<E, Long> getRepository();


    @Override
    public Optional<E> getById(Long id) {
        checkArgument(id != null, "Received incoming parameter null!");  //гугл ультилита- проверка на null
        Optional<E> entity = getRepository().findById(id);
        if (!entity.isPresent()) {
            throw new EntityNotFoundException(entityName.getClass() + "  nety  takogo");
        }
        return entity;
    }

    @Override
    public boolean exists(Long id) {
        checkArgument(id != null, "Received incoming parameter null!");
        return getRepository().existsById(id);
    }


    @Override
    public E create(E entity) {
        log.info("create");
        checkArgument(entity != null, "Not null " + entityName + " is expected!");
        return getRepository().save(entity);
    }

    @Override
    public long count() {
        return getRepository().count();
    }

    @Override
    public E update(E entity) {
        log.info("update");
        checkArgument(entity != null, "Not null " + entityName + " is expected!");
        checkExists(entity.getId());
        return getRepository().save(entity);
    }

    @Override
    public void delete(E entity) {
        log.info("delete ");
        checkArgument(entity != null, "cannot delete!");
        checkExists(entity.getId());
        getRepository().delete(entity);
    }

    @Override
    public void delete(Long id) {
        checkArgument(id != null, "can not delete!");
        checkExists(id);
        getRepository().deleteById(id);
    }

    @Override
    public Iterable<E> getAll(Iterable<Long> ids) {
        log.info("findAll ");
        return getRepository().findAllById(ids);
    }

    @Override
    public Iterable<E> getAll() {
        log.info("findAll ");
        return getRepository().findAll();
    }

    private void checkExists(long id) {
        final boolean exists = getRepository().existsById(id);
        if (!exists) {
            log.error("ID can not be null.");
            throw new EntityNotFoundException(entityName.getClass() + " with id " + id + " not exists!");
        }
    }


}