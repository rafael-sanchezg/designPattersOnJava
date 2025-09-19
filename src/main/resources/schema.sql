CREATE TABLE book (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    type VARCHAR(20) CHECK (type IN ('Fiction', 'No Fiction')),
    format VARCHAR(20) CHECK (format IN ('Physical', 'Digital')),
    state VARCHAR(20) CHECK (state IN ('Available', 'Loaned'))
);

INSERT INTO book (title, author, type, format, state) VALUES
  ('The Hobbit', 'J.R.R. Tolkien', 'Fiction', 'Physical', 'Available'),
  ('A Brief History of Time', 'Stephen Hawking', 'No Fiction', 'Digital', 'Available'),
  ('1984', 'George Orwell', 'Fiction', 'Digital', 'Loaned'),
  ('Clean Code', 'Robert C. Martin', 'No Fiction', 'Physical', 'Available'),
  ('The Pragmatic Programmer', 'Andrew Hunt', 'No Fiction', 'Physical', 'Loaned');
