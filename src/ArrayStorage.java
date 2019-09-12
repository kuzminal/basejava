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
            if (size < storage.length) {  // проверяю не превышен ли размер
                storage[size] = r;
                size++;
            }else {
                System.out.println("Storage is full");
            }
        }else{
            System.out.println("Resume is already exist");
        }
    }

    Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        System.out.println("Resume is not present in list");
        return null;
    }

    void delete(String uuid) {
        boolean hasFound = false;
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                hasFound = true;
                /* добавил смещение элементов чтобы заполнить пустоту
                   все элементы хранятся в начале массива */
                for (int j = i; j < size - 1; j++) { // теперь i = j и ограничение по size -1
                    storage[j] = storage[j + 1];      //копирую в текущий элемент значение следующего
                }
                storage[size-1] = null;            // обнуляю последний элемент
                size--; // после смещение уменьшаю размер массива
            }
        }
        if (!hasFound){
            System.out.println("Resume is not found in storage");
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

    /**
     * HW2
     */
    public void update(Resume resume){
        boolean hasFound = false;
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(resume.uuid)) {
                storage[i] = resume;
                hasFound = true;
            }
        }
        if (!hasFound){
            System.out.println("Resume is not found in storage");
        }
    }
}
