# 🧪 JUnit Test Study — Automated Testing in Spring Boot

> A dedicated **study repository** focused on learning and practising automated testing in Java with **JUnit 5**, **Mockito**, and **Spring Boot Test**. Built over a realistic Spring Boot application stack — including JPA, Spring Security, OpenFeign, and MapStruct — to ensure tests reflect real-world conditions, not toy examples.

---

## 🎯 Purpose

This repository exists for one reason: **to learn how to write good tests**.

Testing is one of the most consistently underpracticed skills in early-career backend development, and one of the first things senior engineers probe in technical interviews. This project was built specifically to close that gap — studying testing concepts in a context that mirrors production code.
---

## 📚 Concepts Studied

### JUnit 5 Fundamentals

| Annotation | What it does |
|---|---|
| `@Test` | Marks a method as a test case |
| `@BeforeEach` | Runs before every test — used for setting up fixtures |
| `@AfterEach` | Runs after every test — used for teardown |
| `@BeforeAll` / `@AfterAll` | Runs once per class — for expensive setup (e.g. DB connections) |
| `@DisplayName` | Human-readable test names in reports |
| `@Nested` | Groups related tests into inner classes for better organisation |
| `@ParameterizedTest` | Runs the same test with multiple input sets |
| `@ValueSource` / `@MethodSource` | Data providers for parameterized tests |
| `@ExtendWith` | Registers extensions (e.g. `MockitoExtension`) |

### Assertions

```java
// Basic assertions
assertEquals(expected, actual);
assertNotNull(result);
assertTrue(result.isPresent());
assertThrows(EntityNotFoundException.class, () -> service.findById(-1L));

// Grouped assertions — all run even if one fails
assertAll(
    () -> assertEquals("Fabricio", user.getName()),
    () -> assertEquals("fabricio@email.com", user.getEmail()),
    () -> assertNotNull(user.getId())
);
```

### Mockito — Isolating the Unit Under Test

The core of unit testing in Spring is **isolating the service layer** from its dependencies (repositories, external clients). Mockito replaces real collaborators with controlled fakes:

```java
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;  // fake — no real DB

    @InjectMocks
    private UsuarioService service;        // real — this is what we're testing

    @Test
    @DisplayName("Should return user when valid ID is provided")
    void shouldReturnUserById() {
        // Arrange — define what the mock returns
        Usuario usuario = new Usuario(1L, "Fabricio", "fabricio@email.com");
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act — call the real service
        UsuarioResponseDTO result = service.buscarPorId(1L);

        // Assert — verify the outcome
        assertNotNull(result);
        assertEquals("Fabricio", result.getNome());

        // Verify — confirm the repository was actually called
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when user is not found")
    void shouldThrowWhenUserNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> service.buscarPorId(99L));
    }
}
```

### Testing Layers

```
┌─────────────────────────────────────────────────────────────┐
│  @WebMvcTest                                                 │
│  Controller layer only · MockMvc · No service or DB         │
├─────────────────────────────────────────────────────────────┤
│  @ExtendWith(MockitoExtension) — Unit Tests                  │
│  One class · All dependencies mocked · Fast                  │
├─────────────────────────────────────────────────────────────┤
│  @SpringBootTest — Integration Tests                         │
│  Full context · Real beans · Tests layer interactions        │
├─────────────────────────────────────────────────────────────┤
│  @DataJpaTest — Repository Tests                             │
│  JPA layer only · In-memory H2 DB · No web layer            │
└─────────────────────────────────────────────────────────────┘
```

---


## 📂 Project Structure

```
src/
├── main/
│   └── java/com/javanauta/
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── model/
│       ├── dto/
│       ├── mapper/
│       └── security/
└── test/
    └── java/com/javanauta/
        ├── controller/    # @WebMvcTest — controller layer tests
        ├── service/       # @ExtendWith(Mockito) — unit tests
        ├── repository/    # @DataJpaTest — JPA layer tests
        └── integration/   # @SpringBootTest — end-to-end flow tests
```

---

## 💡 Key Lessons Learned

**Mocking vs. real beans:** `@Mock` creates a Mockito fake with no behaviour; `@Spy` wraps a real object and lets you override specific methods. `@MockBean` registers the mock inside the Spring context — needed for `@WebMvcTest` and `@SpringBootTest`.

**Test isolation is everything:** A test that depends on another test's side effects is not a test — it's a timing bomb. Every test must set up its own state and leave no trace.

**Testing what matters:** Testing getters and setters is noise. The tests that earn their place verify business rules, edge cases, exception paths, and security enforcement — the code that can go wrong in production.

**The testing pyramid:** Many fast unit tests form the base; fewer, slower integration tests sit above them; even fewer end-to-end tests at the top. Inverting this pyramid (few units, many integration tests) produces a slow, fragile test suite.

---
