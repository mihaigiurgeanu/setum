<#-- arbitraryoffer-macros.ftl -->
<#-- macro used to generate the report -->



<#-- parcurge oferta si grupeaza liniile dupa caracteristici -->
<#function group_line_offers doc>
  <#-- incep cu usile metalice -->
  <#local usi={} />
  <#local sisteme = [] />

  <#local usilines = sel_product(doc, 9990) />
  <#list usilines as usa>
    <#local groupingCode = usa["field[attribute::name='product']/record/field[attribute::name='groupingCode']"] >
    <#local usi = usi + { "groupingCode":((usi[groupingCode]!) + [usa]) } />
  </#list>


  <#-- sisteme -->
  <#local sisteme = sisteme + sel_product(doc, 9998)>
  <#local sisteme = sisteme + sel_product(doc, 9997)>
  <#local sisteme = sisteme + sel_product(doc, 9996)>
  <#local sisteme = sisteme + sel_product(doc, 9995)>
  <#local sisteme = sisteme + sel_product(doc, 9994)>
  <#local sisteme = sisteme + sel_product(doc, 10000)>
  <#local sisteme = sisteme + sel_product(doc, 10001)>
  <#local sisteme = sisteme + sel_product(doc, 10002)>
  <#local sisteme = sisteme + sel_product(doc, 10003)>
  <#local sisteme = sisteme + sel_product(doc, 10004)>
  <#local sisteme = sisteme + sel_product(doc, 10005)>
  <#local sisteme = sisteme + sel_product(doc, 10006)>
  
  <#return ({"usi":usi, "sisteme":sisteme, "usilines":usilines}) >
</#function>

<#-- select the list of products with a given product category -->
<#-- returneaza liniile de oferta -->
<#function sel_product doc categId>
  <#local records=doc["response/record/field[attribute::name='lines']/record[field[attribute::name='product']/record/field[attribute::name='category.id']=${categId?c}]"] />
  <#return records>
</#function>


<#macro display_usi lineno usi>
<!-- display_usi START (${lineno}) -->
<#-- Afiseaza o linie de oferta care grupeaza una sau mai multe usi cu caracteristici similare -->
<#-- lineno este numarul liniei curente (va trebui afisat) -->
<#-- usi este lista de noduri xml de tip record corespunzatoare liniei de oferta (response/record/field[name=lines]/record) -->

<#-- atributele comune le voi lua din prima usa din lista -->
<#assign usa=usi[0]["field[attribute::name='product']/record"] />
<fo:block space-before="10pt">
<fo:inline font-weight="bold">${lineno}. </fo:inline>
Usa metalica &#x00AB;Subcod- ${usa["field[attribute::name='subclass']"]}&#x00BB;,
    &#x00AB;varianta ${usa["field[attribute::name='version']"]}&#x00BB;,
    echipata cu 
    	     &#x00AB;<@system name="broasca">broasca</@system>
	     <@system name="cilindru">cilindru</@system>
	     <@system name="copiatCheie">copiat chei</@system>
	     <@system name="vizor">vizor</@system>
	     <@system name="sild">sild</@system>
	     <@system name="rozeta">rozeta</@system>
	     <@system name="maner">maner</@system>
	     <@system name="yalla1">yalla</@system>
	     <@system name="yalla2">yalla2</@system>
	     <@system name="baraAntipanica">bara antipanica</@system>
	     <@system name="manerSemicilindru">maner semicilindru</@system>
	     <@system name="selectorOrdine">selector ordine</@system>
	     <@system name="amortizor">amortizor</@system>
	     <@system name="alteSisteme1"></@system>
	     <@system name="alteSisteme2"></@system>&#x00BB;


&#x00AB;finisata
	<@finisaj name="intFinisajBlat">blat interior:</@finisaj>
	<@finisaj name="intFinisajToc">toc interior:</@finisaj>
	<@finisaj name="intFinisajGrilaj">grilaj interior:</@finisaj>
	<@finisaj name="intFinisajFereastra">fereastra interior:</@finisaj>
	<@finisaj name="intFinisajSupralumina">supralumina interior:</@finisaj>
	<@finisaj name="intFinisajPanouLateral">panou lateral interior:</@finisaj>
	<@finisaj name="extFinisajBlat">blat exterior:</@finisaj>
	<@finisaj name="extFinisajToc">toc exterior:</@finisaj>
	<@finisaj name="extFinisajGrilaj">grilaj exterior:</@finisaj>
	<@finisaj name="extFinisajFereastra">fereastra exterior:</@finisaj>
	<@finisaj name="extFinisajSupralumina">supralumina exterior:</@finisaj>
	<@finisaj name="extFinisajPanouLateral">panou lateral exterior:</@finisaj>
&#x00BB;
agrement tehnic nr. 
</fo:block>

<fo:table border-collapse="collapse">
  <fo:table-column column-number="1" column-width="1.75cm"/>
  <fo:table-column column-number="2" column-width="1.75cm"/>
  <fo:table-column column-number="3" column-width="1.55cm"/>
  <fo:table-column column-number="4" column-width="1.55cm"/>
  <fo:table-column column-number="5" column-width="4.55cm"/>
  <fo:table-column column-number="6" column-width="1.05cm"/>
  <fo:table-column column-number="7" column-width="1.95cm"/>
  <fo:table-column column-number="8" column-width="1.95cm"/>
  <fo:table-body>
    <fo:table-row>
	<fo:table-cell column-number="1" border-style="solid" font-size="10pt" font-weight="bold" number-rows-spanned="2"><fo:block>COD SETUM</fo:block></fo:table-cell>
	<fo:table-cell column-number="2" border-style="solid" font-size="10pt" font-weight="bold" number-rows-spanned="2"><fo:block>COD BENEF</fo:block></fo:table-cell>
	<fo:table-cell column-number="3" border-style="solid" font-size="10pt" font-weight="bold" number-columns-spanned="2"><fo:block>DIMENSIUNI [mm]</fo:block></fo:table-cell>
	<fo:table-cell column-number="5" border-style="solid" font-size="10pt" font-weight="bold" number-rows-spanned="2"><fo:block>DESCRIERE</fo:block></fo:table-cell>
	<fo:table-cell column-number="6" border-style="solid" font-size="10pt" font-weight="bold" number-rows-spanned="2" text-align="right"><fo:block>BUC</fo:block></fo:table-cell>
	<fo:table-cell column-number="7" border-style="solid" font-size="10pt" font-weight="bold" number-rows-spanned="2" text-align="right"><fo:block>Pret/Buc fara TVA [RON]</fo:block></fo:table-cell>
	<fo:table-cell column-number="8" border-style="solid" font-size="10pt" font-weight="bold" number-rows-spanned="2" text-align="right"><fo:block>Pret/total fara TVA [RON]</fo:block></fo:table-cell>
    </fo:table-row>
    <fo:table-row>
	<fo:table-cell column-number="3" border-style="solid" font-size="10pt" font-weight="bold"><fo:block>Le</fo:block></fo:table-cell>
	<fo:table-cell column-number="4" border-style="solid" font-size="10pt" font-weight="bold"><fo:block>He</fo:block></fo:table-cell>
    </fo:table-row>

    <#list usi as item_usa>
    <#local prod_usa = item_usa["field[attribute::name='product']"] />
    <#local lepl = 0 /> <#-- largime totala panouri laterale -->
    <#local hesl = 0 /> <#-- inaltime totala supralumini -->
      <#list prod_usa["record/field[name='parts']"] as part>
      <#local part_node = part["record/field[name='part']/record"] />
        <#switch part_node["field[name='category.id']"]?number>
	<#case 9980> <#-- panou lateral -->
	<#local lepl = lepl + part_node["field[name='lpl']"]?number />
	<#break/>
	<#case 9981> <#-- supralumina -->
	<#local hesl = hesl + part_node["field[name='hs']"]?number />
	<#break/>
	</#switch>
      </#list>
    <#local lexex = prod_usa["record/field[name='le']"]?number + lepl />
    <#local hexec = prod_usa["record/field[name='he']"]?number + hesl />
    <fo:table-row>
      <fo:table-cell column-number="1" border-style="solid" font-size="10pt"><fo:block>${prod_usa["record/field[name='code']"]}</fo:block></fo:table-cell>
      <fo:table-cell column-number="2" border-style="solid" font-size="10pt"><fo:block>${prod_usa["record/field[name='name']"]}</fo:block></fo:table-cell>
      <fo:table-cell column-number="3" border-style="solid" font-size="10pt"><fo:block>${lexec}</fo:block></fo:table-cell>
      <fo:table-cell column-number="4" border-style="solid" font-size="10pt"><fo:block>${hexec}</fo:block></fo:table-cell>
      <fo:table-cell column-number="5" border-style="solid" font-size="10pt"><fo:block>
          ${prod_usa["record/field[name='k']"]} canat(e),
	  foaie ${search(prod_usa, "intFoil", prod_usa["record/field[name='intFoil']"])} int./${search(prod_usa, "extFoil", prod_usa["record/field[name='extFoil']"])} ext., 
	  <#if prod_usa["record/field[name='k']"]?number > 1>
	  Lcurent = ${prod_usa["record/field[name='lCurrent']"]?number},
	  </#if>
      </fo:block></fo:table-cell>
      <fo:table-cell column-number="6" border-style="solid" font-size="10pt" text-align="right"><fo:block>${item_usa["record/field[name='quantity']"]}</fo:block></fo:table-cell>
      <fo:table-cell column-number="7" border-style="solid" font-size="10pt" text-align="right"><fo:block>${item_usa["record/field[name='price']"]}</fo:block></fo:table-cell>
      <fo:table-cell column-number="8" border-style="solid" font-size="10pt" text-align="right"><fo:block>${item_usa["record/field[name='value']"]}</fo:block></fo:table-cell>
    </fo:table-row>
    </#list>
  </fo:table-body>
</fo:table>



<!-- display_usi END (${lineno}) -->
</#macro>






<#macro display_sisteme lineno sisteme>
<!-- diplay_sisteme START (${lineno}) -->
<#-- Afiseaza o linie de oferta care grupeaza toate sistemele -->
<#-- lineno este numarul liniei curente (va trebui afisat) -->
<#-- usi este lista de noduri xml de tip record corespunzatoare liniei de oferta (response/record/field[name=lines]/record) -->

<!-- display_sisteme END (${lineno}) -->
</#macro>





<#-- afiseaza un sistem dintr-o usa -->
<#macro system name>
  <#if usa["child::field[attribute::name='${name}Id']"]?number &gt; 0>
  <#nested> ${search(usa?parent, "${name}Id", usa["child::field[attribute::name='${name}Id']"])}<#if usa["field[attribute::name='${name}Buc']"]?number &gt; 1 >
	- ${usa["child::field[attribute::name='${name}Buc']"]} buc.</#if>,
  </#if>
</#macro>

<#-- afiseaza finisajul -->
<#macro finisaj name>
  <#if usa["child::field[attribute::name='${name}Id']"]?number != 0>
  <#nested/> ${usa["child::field[attribute::name='${name}']"]};
  </#if> 
</#macro>





<#-- obsolete -->
<#macro "record">
   <#switch .node["child::field[attribute::name='category.id']/child::text()"]?number>
   <#case 9990>
   ${search(.node?parent, "material", .node["child::field[attribute::name='material']"])?cap_first}, 
		Nr canate: ${prop(.node, "k")},
                <!--Nr canate:${.node["child::field[attribute::name='k']"]},-->
		Lg = ${.node["child::field[attribute::name='lg']"]},
		Hg = ${.node["child::field[attribute::name='hg']"]},
                Le = ${.node["child::field[attribute::name='le']"]},
                He = ${.node["child::field[attribute::name='he']"]},
		L util = ${.node["child::field[attribute::name='lUtil']"]},
		H util = ${.node["child::field[attribute::name='hUtil']"]},
		L foaie = ${.node["child::field[attribute::name='lFoaie']"]},
		H foaie = ${.node["child::field[attribute::name='hFoaie']"]},
		Foaie interior: ${search(.node?parent, "intFoil", .node["child::field[attribute::name='intFoil']"])?cap_first},
		Foaie exterior: ${search(.node?parent, "extFoil", .node["child::field[attribute::name='extFoil']"])?cap_first},
		Deschidere spre: ${search(.node?parent, "openingDir", .node["child::field[attribute::name='openingDir']"])?cap_first},
		Deschiderea in: ${search(.node?parent, "openingSide", .node["child::field[attribute::name='openingSide']"])?cap_first},
		Pozitionare foaie: ${search(.node?parent, "foilPosition", .node["child::field[attribute::name='foilPosition']"])?cap_first},
		Tip toc: ${search(.node?parent, "frameType", .node["child::field[attribute::name='frameType']"])?cap_first},
		L toc = ${.node["child::field[attribute::name='lFrame']"]},
		b toc = ${.node["child::field[attribute::name='bFrame']"]},
		c toc = ${.node["child::field[attribute::name='cFrame']"]},
		Tip prag: ${search(.node?parent, "tresholdType", .node["child::field[attribute::name='tresholdType']"])?cap_first},
		L prag = ${.node["child::field[attribute::name='lTreshold']"]},
		H prag = ${.node["child::field[attribute::name='hTreshold']"]},
		C prag = ${.node["child::field[attribute::name='cTreshold']"]},

		<@system name="broasca">Broasca</@system>
		<!--
		<#if .node["child::field[attribute::name='broascaBuc']"]?number &gt; 0>
		Broasca ${search(.node?parent,"broascaId",.node["child::field[attribute::name='broascaId']"])} - ${.node["child::field[attribute::name='broascaBuc']"]} buc.
		</#if>
		-->
		<#if .node["child::field[attribute::name='cilindruBuc']"]?number &gt; 0>
		Cilindru ${search(.node?parent,"cilindruId",.node["child::field[attribute::name='cilindruId']"])} - ${.node["child::field[attribute::name='cilindruBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='copiatCheieBuc']"]?number &gt; 0>
		${search(.node?parent,"copiatCheieId",.node["child::field[attribute::name='copiatCheieId']"])} - ${.node["child::field[attribute::name='copiatCheieBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='vizorBuc']"]?number &gt; 0>
		Vizor ${search(.node?parent,"vizorId",.node["child::field[attribute::name='vizorId']"])} - ${.node["child::field[attribute::name='vizorBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='sildBuc']"]?number &gt; 0>
		Sild ${search(.node?parent,"sildId",.node["child::field[attribute::name='sildId']"])} - ${.node["child::field[attribute::name='sildBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='rozetaBuc']"]?number &gt; 0>
		Rozeta ${search(.node?parent,"rozetaId",.node["child::field[attribute::name='rozetaId']"])} - ${.node["child::field[attribute::name='rozetaBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='manerBuc']"]?number &gt; 0>
		Maner ${search(.node?parent,"manerId",.node["child::field[attribute::name='manerId']"])} - ${.node["child::field[attribute::name='manerBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='yalla1Buc']"]?number &gt; 0>
		Yalla ${search(.node?parent,"yalla1Id",.node["child::field[attribute::name='yalla1Id']"])} - ${.node["child::field[attribute::name='yalla1Buc']"]} buc.
		</#if>

		<#if .node["child::field[attribute::name='yalla2Buc']"]?number &gt; 0>
		Yalla ${search(.node?parent,"yalla2Id",.node["child::field[attribute::name='yalla2Id']"])} - ${.node["child::field[attribute::name='yalla2Buc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='baraAntipanicaBuc']"]?number &gt; 0>
		Bara antipanica ${search(.node?parent,"baraAntipanicaId",.node["child::field[attribute::name='baraAntipanicaId']"])} - ${.node["child::field[attribute::name='baraAntipanicaBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='manerSemicilindruBuc']"]?number &gt; 0>
		Maner semicilindru ${search(.node?parent,"manerSemicilindruId",.node["child::field[attribute::name='manerSemicilindruId']"])} - ${.node["child::field[attribute::name='manerSemicilindruBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='selectorOrdineBuc']"]?number &gt; 0>
		Selector ordine ${search(.node?parent,"selectorOrdineId",.node["child::field[attribute::name='selectorOrdineId']"])} - ${.node["child::field[attribute::name='selectorOrdineBuc']"]} buc.
		</#if>

		<#if .node["child::field[attribute::name='amortizorBuc']"]?number &gt; 0>
		Amortizor ${search(.node?parent,"amortizorId",.node["child::field[attribute::name='amortizorId']"])} - ${.node["child::field[attribute::name='amortizorBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='alteSisteme1Buc']"]?number &gt; 0>
		${search(.node?parent,"alteSisteme1Id",.node["child::field[attribute::name='alteSisteme1Id']"])} - ${.node["child::field[attribute::name='alteSisteme1Buc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='alteSisteme2Buc']"]?number &gt; 0>
		${search(.node?parent,"alteSisteme2Id",.node["child::field[attribute::name='alteSisteme2Id']"])} - ${.node["child::field[attribute::name='alteSisteme2Buc']"]} buc.
		</#if>

    <#if .node["child::field[attribute::name='intFinisajBlatId']"]?number != 0>
    Finisaj int. blat: ${.node["child::field[attribute::name='intFinisajBlat']"]}.
    </#if>
    <#if .node["child::field[attribute::name='intFinisajTocId']"]?number != 0>
    Finisaj int. toc: ${.node["child::field[attribute::name='intFinisajToc']"]}.
    </#if>
    <#if .node["child::field[attribute::name='intFinisajGrilajId']"]?number != 0>
    Finisaj int. grilaj: ${.node["child::field[attribute::name='intFinisajGrilaj']"]}.
    </#if>
    <#if .node["child::field[attribute::name='intFinisajFereastraId']"]?number != 0>
    Finisaj int. fereastra: ${.node["child::field[attribute::name='intFinisajFereastra']"]}.
    </#if>
    <#if .node["child::field[attribute::name='intFinisajSupraluminaId']"]?number != 0>
    Finisaj int. supralumina: ${.node["child::field[attribute::name='intFinisajSupralumina']"]}.
    </#if>
    <#if .node["child::field[attribute::name='intFinisajPanouLateralId']"]?number != 0>
    Finisaj int. panou: ${.node["child::field[attribute::name='intFinisajPanouLateral']"]}.
    </#if>

    <#if .node["child::field[attribute::name='intFinisajBlatId']"]?number != 0>
    Finisaj int. blat: ${.node["child::field[attribute::name='intFinisajBlat']"]}.
    </#if>
    <#if .node["child::field[attribute::name='extFinisajTocId']"]?number != 0>
    Finisaj ext. toc: ${.node["child::field[attribute::name='extFinisajToc']"]}.
    </#if>
    <#if .node["child::field[attribute::name='extFinisajGrilajId']"]?number != 0>
    Finisaj ext. grilaj: ${.node["child::field[attribute::name='extFinisajGrilaj']"]}.
    </#if>
    <#if .node["child::field[attribute::name='extFinisajBlatId']"]?number != 0>
    Finisaj ext. fereastra: ${.node["child::field[attribute::name='extFinisajBlat']"]}.
    </#if>
    <#if .node["child::field[attribute::name='extFinisajSupraluminaId']"]?number != 0>
    Finisaj ext. supralumina: ${.node["child::field[attribute::name='extFinisajSupralumina']"]}.
    </#if>
    <#if .node["child::field[attribute::name='extFinisajPanouLateralId']"]?number != 0>
    Finisaj ext. panou: ${.node["child::field[attribute::name='extFinisajPanouLateral']"]}.
    </#if>


    <#list .node["child::field[attribute::name='parts']/child::record/child::field[attribute::name='part']/child::record"] as record>
    <fo:block>
    <#visit record>
    </fo:block>
    </#list>

  <#break>

  <#case 9980>
  Panou lateral ${.node["child::field[attribute::name='lpl']"]} mm X ${.node["child::field[attribute::name='hpl']"]} mm
  avand ${.node["child::field[attribute::name='cells']"]} celule -
  ${.node["child::field[attribute::name='quantity']"]} buc.
  <#break>

  <#case 9981>
  Supralumina ${.node["child::field[attribute::name='ls']"]} mm X ${.node["child::field[attribute::name='hs']"]} mm
  avand ${.node["child::field[attribute::name='cells']"]} celule
  ${.node["child::field[attribute::name='quantity']"]} buc.
  <#break>

  <#case 9982>
  Gauri aerisire cu diametrul ${.node["child::field[attribute::name='diametru']"]} mm,
  pasul ${.node["child::field[attribute::name='pas']"]} mm
  ${.node["child::field[attribute::name='nrRanduri']"]} randuri.
  <#break>

  <#case 9983>
  Grila ventilatie ${.node["child::field[attribute::name='lgv']"]} mm X ${.node["child::field[attribute::name='hgv']"]} mm
  ${.node["child::field[attribute::name='quantity']"]} buc.
  <#break>
  
  <#case 9989>
  Fereastra ${.node["child::field[attribute::name='lf']"]} mm X ${.node["child::field[attribute::name='hf']"]} mm
  ${.node["child::field[attribute::name='quantity']"]} buc.
  <#break>
  
  <#default>
  ${.node["child::field[attribute::name='category.name']"]?cap_first}
  ${.node["child::field[attribute::name='code']"]}:
  <@partslisting .node/>
  
  </#switch>

</#macro>




<#macro "partslisting" node>

<#local records = node["child::field[attribute::name='parts']/child::record/child::field[attribute::name='part']/child::record"]>

<#if records?size &gt; 0>
${record["child::field[attribute::name='category.name']"]} 
${record["child::field[attribute::name='code']"]};
</#if>
</#macro>



<#-- search a value-list for a value coresponding to a value-id -->
<#function search node vl_name vl_key>


<#-- 
Searches the node for a child value-list with the given name, and in the
value list searches a child with the give key (value) and returns the associated label
-->


<#list node["value-list"] as vl>
	<#if vl["@name"] = vl_name>
		<#list vl["vl-item"] as vl_item>
			<#if vl_item.value = vl_key>
				<#return vl_item.label>
			</#if>
		</#list>
	</#if>
</#list>

<#return "N/A">

</#function>


<#-- selecteaza un atribut al nodului dat -->
<#function prop node name>
  <#return node["child::field[attribute::name='${name}']"]>
</#function>
