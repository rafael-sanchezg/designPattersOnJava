package com.pocs.designpatterns.designpattersonjava.domain.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ValidationChainFactory.
 */
@DisplayName("ValidationChainFactory Tests")
class ValidationChainFactoryTest {

    @Test
    @DisplayName("Should create complete validation chain")
    void shouldCreateCompleteValidationChain() {
        BookValidator chain = ValidationChainFactory.createCompleteValidationChain();

        assertNotNull(chain);
        assertTrue(chain instanceof TitleValidator);
    }

    @Test
    @DisplayName("Should create basic validation chain")
    void shouldCreateBasicValidationChain() {
        BookValidator chain = ValidationChainFactory.createBasicValidationChain();

        assertNotNull(chain);
        assertTrue(chain instanceof TitleValidator);
    }

    @Test
    @DisplayName("Basic chain should validate title and author")
    void basicChainShouldValidateTitleAndAuthor() {
        BookValidator chain = ValidationChainFactory.createBasicValidationChain();

        // Valid data should pass
        assertDoesNotThrow(() -> chain.validate("Clean Code", "Robert Martin"));

        // Invalid title should fail
        assertThrows(ValidationException.class, () -> chain.validate("", "Robert Martin"));

        // Invalid author should fail
        assertThrows(ValidationException.class, () -> chain.validate("Clean Code", "M"));
    }

    @Test
    @DisplayName("Should create title validation chain")
    void shouldCreateTitleValidationChain() {
        BookValidator chain = ValidationChainFactory.createTitleValidationChain();

        assertNotNull(chain);
        assertTrue(chain instanceof TitleValidator);
    }

    @Test
    @DisplayName("Should create author validation chain")
    void shouldCreateAuthorValidationChain() {
        BookValidator chain = ValidationChainFactory.createAuthorValidationChain();

        assertNotNull(chain);
        assertTrue(chain instanceof AuthorValidator);
    }

    @Test
    @DisplayName("Should create custom chain")
    void shouldCreateCustomChain() {
        BookValidator title = new TitleValidator();
        BookValidator author = new AuthorValidator();

        BookValidator chain = ValidationChainFactory.createCustomChain(title, author);

        assertNotNull(chain);
        assertEquals(title, chain);
    }

    @Test
    @DisplayName("Should fail when creating custom chain with no validators")
    void shouldFailWhenCreatingCustomChainWithNoValidators() {
        assertThrows(IllegalArgumentException.class,
            () -> ValidationChainFactory.createCustomChain());
    }

    @Test
    @DisplayName("Should fail when creating custom chain with null")
    void shouldFailWhenCreatingCustomChainWithNull() {
        assertThrows(IllegalArgumentException.class,
            () -> ValidationChainFactory.createCustomChain((BookValidator[]) null));
    }

    @Test
    @DisplayName("Custom chain should link validators correctly")
    void customChainShouldLinkValidatorsCorrectly() {
        BookValidator title = new TitleValidator();
        BookValidator author = new AuthorValidator();

        BookValidator chain = ValidationChainFactory.createCustomChain(title, author);

        // Should pass through both validators
        assertDoesNotThrow(() -> chain.validate("Valid Title", "Valid Author"));

        // Should fail at first validator
        assertThrows(ValidationException.class, () -> chain.validate("", "Valid Author"));

        // Should pass first but fail at second
        assertThrows(ValidationException.class, () -> chain.validate("Valid Title", "X"));
    }
}

