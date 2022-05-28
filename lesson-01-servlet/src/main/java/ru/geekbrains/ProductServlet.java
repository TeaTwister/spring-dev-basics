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
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/product/*")
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
        List<Product> products = new ArrayList<>();
        PrintWriter writer = resp.getWriter();
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.length() > 1) {
            Product product;
            long id;
            try {
                id = Long.parseLong(pathInfo.substring(1));
            } catch (NumberFormatException nfe) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.println("<p>Not a number<p/>");
                return;
            }
            product = productRepository.findById(id);
            if (product != null) {
                products.add(product);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                writer.println("<p>No such product<p/>");
                return;
            }
        } else {
            products.addAll(productRepository.findAll());
        }

        writer.println("<table>");
        writer.print(P_TR);
        writer.print("<th>Id</th>");
        writer.print("<th>Product</th>");
        writer.print("<th>Cost</th>");
        writer.println(P_TR_);

        for (Product p : products) {
            writer.print(P_TR);
            printTableItem(writer, p.getId());
            printTableItem(writer, p.getTitle());
            printTableItem(writer, p.getCost());
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
