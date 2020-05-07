import scrapy
 
class LoginSpider(scrapy.Spider):
    name = 'login-spider'
    start_urls = ['http://quotes.toscrape.com/login']
 
    def parse(self, response):
        # depois a gente vai implementar aqui o preenchimento do formulário
        # contido em response e fazer a requisição de login
        self.log('visitei a página de login: {}'.format(response.url))
 
    def parse_author_links(self, response):
        # aqui a gente vai extrair os links para as páginas dos autores
        # ou seja, quando chegar aqui, o spider já estará autenticado
        pass
