package tasks;

import common.Person;
import common.Task;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
А теперь о горьком
Всем придется читать код
А некоторым придется читать код, написанный мною
Сочувствую им
Спасите будущих жертв, и исправьте здесь все, что вам не по душе!
P.S. функции тут разные и рабочие (наверное), но вот их понятность и эффективность страдает (аж пришлось писать комменты)
P.P.S Здесь ваши правки желательно прокомментировать (можно на гитхабе в пулл реквесте)
 */
public class Task8 implements Task {

  private long count;

  //Не хотим выдывать апи нашу фальшивую персону, поэтому конвертим начиная со второй
  public List<String> getNames(List<Person> persons) {
    /*
     * Вызов remove(0) приводит к изменению входного списка persons вне этой функции,
     * так как persons - это указатель на список.
     * Каждый вызов функции getNames будет удалять первый элемент списка, что вероятно является ошибкой.
     * Проверку на нулевой размер можно убрать, так как stream сам вернет emptyList в случае persons.size() == 0.
     */
    return persons.stream().skip(1).map(Person::getFirstName).collect(Collectors.toList());
  }

  //ну и различные имена тоже хочется
  public Set<String> getDifferentNames(List<Person> persons) {
    /*
     * нет необходимости создавать поток и работать с ним,
     * если есть возможность создать одну коллекцию для выполнения всех действий сразу
     */
    return new HashSet<>(getNames(persons));
  }

  //Для фронтов выдадим полное имя, а то сами не могут
  public String convertPersonToString(Person person) {
    /*
     * В функции опечатка, getSecondName вызывается дважды.
     * Полное имя можно собрать с помощью stream, убрав из потока элементы, если они null.
     * (Необходимый формат полного имени нужно уточнить)
     */
    return Stream.of(person.getSecondName(), person.getFirstName(), person.getMiddleName()).
            filter(Objects::nonNull).
            collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    /*
     * Можно сделать с помощью stream и Collectors.toMap
     * Если не нужно убирать фейковую персону, то убрать skip(1)
     */
    return persons.stream().skip(1).collect(Collectors.toMap(Person::getId, this::convertPersonToString, (a, b) -> a));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    // Можно сделать с помощью stream
    return persons1.stream().anyMatch(persons2::contains);
  }

  //...
  public long countEven(Stream<Integer> numbers) {
    // Вместо отдельного счетчика можно воспользоваться методом stream
    return numbers.filter(num -> num % 2 == 0).count();
  }

  @Override
  public boolean check() {
    System.out.println("Проверка исправленных методов");
    List<Person> persons = new ArrayList<>(List.of(
            new Person(1, "Fake", Instant.now()),
            new Person(2, "Vasya", Instant.now()),
            new Person(3, "Ivan", "Ivanov", "Ivanovich", Instant.ofEpochMilli(100500)),
            new Person(4, "Ivan", Instant.now()),
            new Person(5, "Denis", Instant.now()),
            new Person(6, "Boris", Instant.now())
    ));

    List<Person> persons2 = new ArrayList<>(List.of(
            new Person(1, "Fake", Instant.now()),
            new Person(3, "Ivan", "Ivanov", "Ivanovich", Instant.ofEpochMilli(100500))
    ));

    boolean everythingIsOk = true;
    everythingIsOk &= getNames(persons).equals(Arrays.asList("Vasya", "Ivan", "Ivan", "Denis", "Boris"));
    everythingIsOk &= getDifferentNames(persons).equals(Set.of("Vasya", "Ivan", "Denis", "Boris"));
    everythingIsOk &= convertPersonToString(persons.get(2)).equals("Ivanov Ivan Ivanovich");
    everythingIsOk &= getPersonNames(persons).
            equals(Map.of(2, "Vasya",
                          3, "Ivanov Ivan Ivanovich",
                          4, "Ivan",
                          5, "Denis",
                          6, "Boris"));
    everythingIsOk &= hasSamePersons(persons, persons2);
    everythingIsOk &= (countEven(Stream.of(1, 2, 3, 4, 5, 6)) == 3);

    return everythingIsOk;
  }
}
