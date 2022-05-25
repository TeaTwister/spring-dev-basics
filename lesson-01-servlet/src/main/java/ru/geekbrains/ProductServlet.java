package ru.geekbrains;

import ru.geekbrains.persist.Product;
import ru.geekbrains.persist.ProductRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet(urlPatterns = "/product")
public class ProductServlet extends HttpServlet {

    public static final String P_TR = "<tr>";
    public static final String P_TR_ = "</tr>";
    public static final String P_TD = "<td>";
    public static final String P_TD_ = "</td>";

    private ProductRepository productRepository;

    @Override
    public void init() throws ServletException {
        productRepository = new ProductRepository();
        BigDecimal baseCost = BigDecimal.valueOf(0.99d);
        for (int i = 1; i <= 10; i++) {
            productRepository.insert(
                    new Product(
                            String.format("Product %d", i),
                            baseCost.multiply(BigDecimal.valueOf(i))
                    )
            );
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("<table>");
        writer.print(P_TR);
        writer.print("<th>Id</th>");
        writer.print("<th>Product</th>");
        writer.print("<th>Cost</th>");
        writer.println(P_TR_);

        for (Product product : productRepository.findAll()) {
            writer.print(P_TR);
            printTableItem(writer, product.getId());
            printTableItem(writer, product.getTitle());
            printTableItem(writer, product.getCost());
            writer.println(P_TR_);
        }

        writer.println("</table>");
    }

    private void printTableItem(PrintWriter writer, Object item) {
        writer.print(P_TD);
        writer.print(item);
        writer.print(P_TD_);
    }
}
