package ex03;

import static spark.Spark.*;

public class Aplicacao {
    
    private static ProdutosService produtosService = new ProdutosService(); 
    
    public static void main(String[] args) {
        port(6789); 
        
        staticFiles.location("/public");
        
        post("/produto/insert", (request, response) -> produtosService.insert(request, response));

        get("/produto/:id", (request, response) -> produtosService.get(request, response));
        
        get("/produto/list/:orderby", (request, response) -> produtosService.getAll(request, response));

        get("/produto/update/:id", (request, response) -> produtosService.getToUpdate(request, response));
        
        post("/produto/update/:id", (request, response) -> produtosService.update(request, response));
            
        get("/produto/delete/:id", (request, response) -> produtosService.delete(request, response));
    }
}