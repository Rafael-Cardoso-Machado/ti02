package ex03;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import spark.Request;
import spark.Response;

public class ProdutosService {

    private ProdutosDAO produtoDAO = new ProdutosDAO();
    private String form;
    private final int FORM_INSERT = 1;
    private final int FORM_DETAIL = 2;
    private final int FORM_UPDATE = 3;
    
    private final int FORM_ORDERBY_CODIGO = 1;
    private final int FORM_ORDERBY_NOME = 2;
    private final int FORM_ORDERBY_PRECO = 3;

    public ProdutosService() {
        makeForm();
    }

    public void makeForm() {
        makeForm(FORM_INSERT, new Produtos(), FORM_ORDERBY_NOME);
    }

    public void makeForm(int orderBy) {
        makeForm(FORM_INSERT, new Produtos(), orderBy);
    }

    public void makeForm(int tipo, Produtos produto, int orderBy) {
        String nomeArquivo = "src/main/resources/index.html";
        form = "";
        try {
            Scanner entrada = new Scanner(new File(nomeArquivo));
            while (entrada.hasNext()) {
                form += (entrada.nextLine() + "\n");
            }
            entrada.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String umProduto = "";
        if (tipo != FORM_INSERT) {
            umProduto += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umProduto += "\t\t<tr>";
            umProduto += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/produto/list/1\">Novo Produto</a></b></font></td>";
            umProduto += "\t\t</tr>";
            umProduto += "\t</table>";
            umProduto += "\t<br>";
        }

        if (tipo == FORM_INSERT || tipo == FORM_UPDATE) {
            String action = "/produto/";
            String name, nomeProduto, buttonLabel;
            
            String campoCodigo = "";
            
            if (tipo == FORM_INSERT) {
                action += "insert";
                name = "Inserir Produto";
                nomeProduto = " ";
                buttonLabel = "Inserir";
                campoCodigo = "\t\t\t<td>&nbsp;Código: <input class=\"input--register\" type=\"number\" name=\"codigo\" value=\"\"></td>\n";
            } else {
                action += "update/" + produto.getCodigo();
                name = "Atualizar Produto (Código " + produto.getCodigo() + ")";
                nomeProduto = produto.getNome();
                buttonLabel = "Atualizar";
                campoCodigo = "\t\t\t<td>&nbsp;Código: " + produto.getCodigo() + "</td>\n" 
                            + "<input type=\"hidden\" name=\"codigo\" value=\"" + produto.getCodigo() + "\">";
            }
            umProduto += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
            umProduto += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umProduto += "\t\t<tr><td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td></tr>";
            umProduto += "\t\t<tr><td colspan=\"3\" align=\"left\">&nbsp;</td></tr>";
            umProduto += "\t\t<tr>";
            umProduto += campoCodigo;
            umProduto += "\t\t\t<td>&nbsp;Nome: <input class=\"input--register\" type=\"text\" name=\"nome\" value=\"" + nomeProduto + "\"></td>";
            umProduto += "\t\t\t<td>Preço: <input class=\"input--register\" type=\"number\" step=\"0.01\" name=\"preco\" value=\"" + produto.getPreco() + "\"></td>";
            umProduto += "\t\t</tr>";
            umProduto += "\t\t<tr>";
            umProduto += "\t\t\t<td colspan=\"3\" align=\"center\"><input type=\"submit\" value=\"" + buttonLabel + "\" class=\"input--main__style input--button\"></td>";
            umProduto += "\t\t</tr>";
            umProduto += "\t</table>";
            umProduto += "\t</form>";
        } else if (tipo == FORM_DETAIL) {
            umProduto += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umProduto += "\t\t<tr><td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Produto (Código " + produto.getCodigo() + ")</b></font></td></tr>";
            umProduto += "\t\t<tr><td colspan=\"3\" align=\"left\">&nbsp;</td></tr>";
            umProduto += "\t\t<tr>";
            umProduto += "\t\t\t<td>&nbsp;Nome: " + produto.getNome() + "</td>";
            umProduto += "\t\t\t<td>Preço: " + produto.getPreco() + "</td>";
            umProduto += "\t\t\t<td>&nbsp;</td>";
            umProduto += "\t\t</tr>";
            umProduto += "\t</table>";
        } else {
            System.out.println("ERRO! Tipo não identificado " + tipo);
        }
        form = form.replaceFirst("<UM-PRODUTO>", umProduto);

        String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
        list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Produtos</b></font></td></tr>\n"
              + "\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n"
              + "\n<tr>\n"
              + "\t<td><a href=\"/produto/list/" + FORM_ORDERBY_CODIGO + "\"><b>Código</b></a></td>\n"
              + "\t<td><a href=\"/produto/list/" + FORM_ORDERBY_NOME + "\"><b>Nome</b></a></td>\n"
              + "\t<td><a href=\"/produto/list/" + FORM_ORDERBY_PRECO + "\"><b>Preço</b></a></td>\n"
              + "\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n"
              + "\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n"
              + "\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n"
              + "</tr>\n";

        List<Produtos> produtos;
        if (orderBy == FORM_ORDERBY_CODIGO) {      produtos = produtoDAO.getOrderByCodigo();
        } else if (orderBy == FORM_ORDERBY_NOME) {   produtos = produtoDAO.getOrderByNome();
        } else if (orderBy == FORM_ORDERBY_PRECO) {  produtos = produtoDAO.getOrderByPreco();
        } else {                                     produtos = produtoDAO.get();
        }

        int i = 0;
        String bgcolor = "";
        for (Produtos p : produtos) {
            bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
            list += "\n<tr bgcolor=\"" + bgcolor + "\">\n"
                  + "\t<td>" + p.getCodigo() + "</td>\n"
                  + "\t<td>" + p.getNome() + "</td>\n"
                  + "\t<td>" + p.getPreco() + "</td>\n"
                  + "\t<td align=\"center\" valign=\"middle\"><a href=\"/produto/" + p.getCodigo() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n"
                  + "\t<td align=\"center\" valign=\"middle\"><a href=\"/produto/update/" + p.getCodigo() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n"
                  + "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteProduto('" + p.getCodigo() + "', '" + p.getNome() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n"
                  + "</tr>\n";
        }
        list += "</table>";
        form = form.replaceFirst("<LISTAR-PRODUTO>", list);
    }

    public Object insert(Request request, Response response) {
        int codigo = Integer.parseInt(request.queryParams("codigo"));
        String nome = request.queryParams("nome");
        double preco = Double.parseDouble(request.queryParams("preco"));

        String resp = "";
        Produtos produto = new Produtos(codigo, nome, preco);

        if (produtoDAO.insert(produto)) {
            resp = "Produto (" + nome + ") inserido!";
            response.status(201); // 201 Created
        } else {
            resp = "Produto (" + nome + ") não inserido!";
            response.status(404); // 404 Not found
        }

        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object get(Request request, Response response) {
        int codigo = Integer.parseInt(request.params(":id"));
        Produtos produto = produtoDAO.get(codigo);

        if (produto != null) {
            response.status(200); // success
            makeForm(FORM_DETAIL, produto, FORM_ORDERBY_NOME);
        } else {
            response.status(404); // 404 Not found
            String resp = "Produto " + codigo + " não encontrado.";
            makeForm();
            form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
        }
        return form;
    }

    public Object getToUpdate(Request request, Response response) {
        int codigo = Integer.parseInt(request.params(":id"));
        Produtos produto = produtoDAO.get(codigo);

        if (produto != null) {
            response.status(200); // success
            makeForm(FORM_UPDATE, produto, FORM_ORDERBY_NOME);
        } else {
            response.status(404); // 404 Not found
            String resp = "Produto " + codigo + " não encontrado.";
            makeForm();
            form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
        }
        return form;
    }

    public Object getAll(Request request, Response response) {
        int orderBy = Integer.parseInt(request.params(":orderby"));
        makeForm(orderBy);
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        return form;
    }

    public Object update(Request request, Response response) {
        int codigo = Integer.parseInt(request.params(":id"));
        Produtos produto = produtoDAO.get(codigo);
        String resp = "";

        if (produto != null) {
            produto.setNome(request.queryParams("nome"));
            produto.setPreco(Double.parseDouble(request.queryParams("preco")));
            
            produtoDAO.update(produto);
            response.status(200); // success
            resp = "Produto (Código " + produto.getCodigo() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Produto (Código " + codigo + ") não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object delete(Request request, Response response) {
        int codigo = Integer.parseInt(request.params(":id"));
        Produtos produto = produtoDAO.get(codigo);
        String resp = "";

        if (produto != null) {
            produtoDAO.delete(codigo);
            response.status(200); // success
            resp = "Produto (" + codigo + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Produto (" + codigo + ") não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }
}