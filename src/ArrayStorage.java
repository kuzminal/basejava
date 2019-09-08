/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    void clear() {
        int size = size();
        for (int i=0; i < size; i++) {
            if (storage[i] != null) storage[i] = null;
        }
    }

    void save(Resume r) {
        if (get(r.uuid) == null ) //проверят нет ли уже записи с таким же UUID
            storage[size()] = r;
    }

    Resume get(String uuid) {
        Resume result = null;
        int size = size();
        for (int i=0; i < size; i++) {
            if (storage[i] != null && storage[i].uuid.equals(uuid))
                result = storage[i];
        }
        return result;
    }

    void delete(String uuid) {
        int size = size();
        for (int i=0; i < size; i++) {
            if (storage[i] != null && storage[i].uuid.equals(uuid)) {
                storage[i] = null;
                for (int j = i + 1; j < size; j++) { // добавил смещение элементов чтобы заполнить пустоту
                    if (storage[j] != null) {                  // все элементы хранятся вначале массива
                        storage[j-1] = storage[j];
                        storage[j] = null;
                    }
                }
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] listResume = new Resume[size()];
        for (int i=0; i < size(); i++) {
            if (storage[i] != null) listResume[i] = storage[i];
        }
        return listResume;
    }

    int size() {
        int size = 0;
        for (int i=0; i < storage.length; i++) {
            if (storage[i] != null) size++;
        }
        return size;
    }
}
