package com.martin;

import lombok.Data;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertTrue;

public class AppTest
{
    private static final List<String> STRINGS = asList("first", "second", "third");
    private static final List<Integer> INTEGERS = asList(1, 2, 4, 5, 7, 8);

    private static final List<Person> PEOPLE = asList(
            new Person("Jack", asList(new Pet(2)), emptyList()),
            new Person("Jane", asList(new Pet(2)), emptyList()),
            new Person("John", asList(new Pet(4)), asList(new Car("Trabant")))
    );

    @Test
    public void testNgListAssertions()
    {
        // do NOT do this
        assertTrue(STRINGS.contains("first"));
        assertTrue(!STRINGS.isEmpty());

//        assertTrue(STRINGS.contains("fourth"));
//        assertTrue(STRINGS.isEmpty());
    }

    @Test
    public void assertJListAssertions()
    {
        assertThat(STRINGS).contains("first");
        assertThat(STRINGS).isNotEmpty();
        assertThat(STRINGS).containsExactlyInAnyOrderElementsOf(asList("second", "first", "third"));

//        assertThat(STRINGS).contains("fourth");
//        assertThat(STRINGS).isEmpty();
//        assertThat(INTEGERS).containsExactlyInAnyOrderElementsOf(Arrays.asList(5, 6));
    }

    @Test
    public void assertJDateAssertions()
    {
        assertThat(LocalDate.of(2018, 2, 24)).isAfter(LocalDate.of(2018, 1, 1));
    }

    @Test
    public void assertJSortingAssertion()
    {
        assertThat(INTEGERS).isSorted();

        assertThat(PEOPLE).isSortedAccordingTo(Comparator.comparing(Person::getName));
    }

    @Test
    public void assertOptionalAssertion()
    {
        assertThat(Optional.of(new Car("Trabant"))).isPresent();
    }

    @Test
    public void assertJChainedAssertions()
    {
        assertThat(PEOPLE).as("List of people is as expected.")
                          .isNotEmpty()
                          .hasSize(3)
                          .flatExtracting(Person::getPets)
                          .allSatisfy(pet -> assertThat(pet.getNumberOfLegs()).as("Pet legs should be correct.").isBetween(1, 4));

        // TestNG alternative
//        assertTrue(PEOPLE.stream()
//                         .map(Person::getPets)
//                         .flatMap(Collection::stream)
//                         .allMatch(pet -> pet.getNumberOfLegs() > 3), "Every pet should have at least 3 legs.");
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

