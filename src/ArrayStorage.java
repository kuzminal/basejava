/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int size = 0;

    void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    void save(Resume r) {
        if (get(r.uuid) == null) { //проверят нет ли уже записи с таким же UUID
            storage[size] = r;
            size++;
        }
    }

    Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                /* добавил смещение элементов чтобы заполнить пустоту
                   все элементы хранятся в начале массива */
                for (int j = i ; j < size - 1; j++) { // теперь i = j и ограничение по size -1
                    storage[j] = storage[j + 1];      //копирую в текущий элемент значение следующего
                    storage[j + 1] = null;            // обнуляю следующий элемент
                }
                size--; // после смещение уменьшаю размер массива
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] listResume = new Resume[size()];
        for (int i = 0; i < size; i++) {
            listResume[i] = storage[i];
        }
        return listResume;
    }

    int size() {
        return size;
    }
}
