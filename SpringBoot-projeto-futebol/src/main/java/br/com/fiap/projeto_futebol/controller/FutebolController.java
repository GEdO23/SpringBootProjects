package br.com.fiap.projeto_futebol.controller;

import br.com.fiap.projeto_futebol.model.Jogador;
import br.com.fiap.projeto_futebol.model.ListaUF;
import br.com.fiap.projeto_futebol.model.Time;
import br.com.fiap.projeto_futebol.repository.JogadorRespository;
import br.com.fiap.projeto_futebol.repository.TimeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class FutebolController {

    @Autowired
    private TimeRepository repT;

    @Autowired
    private JogadorRespository repJ;

    @GetMapping("/retorna_lista_jogadores")
    public ModelAndView retornaListaJogadores() {

        List<Jogador> listaJ = repJ.findAll();

        ModelAndView mv = new ModelAndView("index");
        mv.addObject("jogadores", listaJ);

        return mv;
    }

    @GetMapping("/retornaFormCadTime")
    public ModelAndView retornaFormCadTime() {
        ModelAndView mv = new ModelAndView("form_cad_times");
        mv.addObject("time", new Time());
        mv.addObject("lista_uf", ListaUF.values());
        return mv;
    }

    @PostMapping("/inserir_time")
    public ModelAndView cadastroTime(@Valid Time novo_time, BindingResult bd) {

        if (bd.hasErrors()) {
            ModelAndView mv = new ModelAndView("form_cad_times");
            mv.addObject("time", novo_time);
            mv.addObject("lista_uf", ListaUF.values());

			return mv;
        }

        Time t = new Time();
        t.setNome(novo_time.getNome());
        t.setBrasao(novo_time.getBrasao());
        t.setUf(novo_time.getUf());
        t.setHino(novo_time.getHino());

        repT.save(t);

        return new ModelAndView("redirect:/retorna_lista_jogadores");
    }

    @GetMapping("/detalhes_jogador/{id}")
    public ModelAndView retornaDetalhesJogador(@PathVariable Long id) {
        Optional<Jogador> op = repJ.findById(id);
        if (op.isEmpty()) return new ModelAndView("redirect:/retorna_lista_jogadores");

        Jogador jogador = op.get();
        ModelAndView mv = new ModelAndView("detalhes_jogador");
        mv.addObject("jogador", jogador);

        return mv;
    }
}
