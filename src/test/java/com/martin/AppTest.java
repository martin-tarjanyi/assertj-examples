package com.martin;

import lombok.Data;
import org.assertj.core.api.Condition;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AppTest
{
    @Test
    public void testNgListAssertions()
    {
        List<String> stringList = asList("first", "second", "third");

        // do NOT do this
        assertTrue(stringList.contains("five"));
        assertTrue(stringList.isEmpty());
        assertEquals(stringList, asList("third", "fourth"));
    }

    @Test
    public void assertJListAssertions()
    {
        List<String> stringList = asList("first", "second", "third");
        List<Integer> integerList = asList(1, 2, 4, 5, 7, 8);

        assertThat(stringList).contains("first");
        assertThat(stringList).isNotEmpty();
        assertThat(stringList).containsExactlyInAnyOrderElementsOf(asList("second", "first", "third"));

//        assertThat(stringList).contains("fourth");
//        assertThat(stringList).isEmpty();
//        assertThat(integerList).isEqualTo(asList(5, 6));
//        assertThat(integerList).containsExactlyInAnyOrderElementsOf(asList(5, 6));
    }

    @Test
    public void assertJDateAssertions()
    {
        assertThat(LocalDate.of(2018, 2, 24)).isBefore(LocalDate.of(2018, 1, 1));
    }

    @Test
    public void assertJSortingAssertion()
    {
        List<Integer> integerList = asList(1, 2, 5, 4, 7, 8);

        List<Person> personList = asList(
                new Person("Zach", asList(new Pet(2)), emptyList()),
                new Person("Jack", asList(new Pet(2)), emptyList()),
                new Person("Bill", asList(new Pet(4)), asList(new Car("Trabant")))
        );

        assertThat(integerList).isSorted();
        assertThat(personList).as("Persons should be orderedy by name.")
                              .isSortedAccordingTo(Comparator.comparing(Person::getName));
    }

    @Test
    public void assertOptionalAssertion()
    {
        assertThat(Optional.of(new Car("Trabant"))).isEmpty();
    }

    @Test
    public void assertJChainedAssertions()
    {
        List<Person> personList = asList(
                new Person("Yach", asList(new Pet(2)), emptyList()),
                new Person("Zach", asList(new Pet(2)), emptyList()),
                new Person("Bob", asList(new Pet(2)), emptyList()),
                new Person("Bill", asList(new Pet(4)), asList(new Car("Trabant")))
        );

        assertThat(personList).as("List of people is as expected.")
                              .isNotEmpty()
                              .hasSize(4)
                              .allSatisfy(person -> assertThat(person.getCars()).as("All peoples should have a car.").size().isBetween(1, 3));

        assertThat(personList).areExactly(2, new Condition<>(person -> person.getName()
                                                                             .startsWith("B"), "Person name should start with Z"))
                              .areExactly(1, new Condition<>(person -> person.getName()
                                                                             .startsWith("Z"), "Person name should start with Z"))
                              .areNot(new Condition<>(person -> person.getName().startsWith("Y"), "Person name should start with Y"));
        // TestNG alternative
//        assertTrue(PEOPLE.stream()
//                         .map(Person::getPets)
//                         .flatMap(Collection::stream)
//                         .allMatch(pet -> pet.getNumberOfLegs() > 3), "Every pet should have at least 3 legs.");
    }

    @Test
    public void assertJNumberComparisons()
    {
        assertThat(5).isGreaterThanOrEqualTo(6);
        assertThat(BigDecimal.TEN).isBetween(BigDecimal.ZERO, BigDecimal.ONE);
    }

    @Test
    public void assertJStringAssertions()
    {
        assertThat("some text").startsWith("Other");
        assertThat("some text").contains("any");
        assertThat("791a0").containsOnlyDigits();
    }
}

@Data
class Person
{
    private final String name;
    private final List<Pet> pets;
    private final List<Car> cars;
}

@Data
class Pet
{
    private final int numberOfLegs;
}

@Data
class Car
{
    private final String name;
}

