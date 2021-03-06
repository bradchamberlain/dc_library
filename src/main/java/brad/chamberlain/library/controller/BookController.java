package brad.chamberlain.library.controller;

import brad.chamberlain.library.model.Book;
import brad.chamberlain.library.model.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple Spring boot controller to handle CRUD requests for my book library.
 */
@RestController
@RequestMapping("/api/v1")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/books/{id}")
    public ResponseEntity< Book > getBookById(@PathVariable(value = "id") Long bookId)
            throws ResourceNotFoundException{
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(bookId));
        return ResponseEntity.ok().body(book);
    }

    @PostMapping("/books")
    public Book createBook(@Valid @RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity < Book > updateBook(@PathVariable(value = "id") Long bookId,
                                                      @Valid @RequestBody Book bookDetails) throws ResourceNotFoundException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(bookId));

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setPages(bookDetails.getPages());
        final Book updatedBook = bookRepository.save(book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/books/{id}")
    public Map< String, Boolean > deleteBook(@PathVariable(value = "id") Long bookId)
            throws ResourceNotFoundException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(bookId));

        bookRepository.delete(book);
        Map < String, Boolean > response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends Exception{

        private static final long serialVersionUID = 1L;

        public ResourceNotFoundException(long id){
            super("Book not found for this id :: " + id);
        }
    }
}