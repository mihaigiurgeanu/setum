<?xml version="1.0" encoding="iso-8859-2"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <fo:layout-master-set>
    <fo:simple-page-master master-name="page"
			   page-height="297mm"
			   page-width="210mm"
			   margin-top="20mm"
			   margin-bottom="10mm"
			   margin-left="25mm"
			   margin-right="25mm">
      <fo:region-body margin-top="40mm" margin-bottom="15mm"
		      margin-left="0mm" margin-right="0mm"/>
      <fo:region-before extent="30mm"/>
      <fo:region-after extent="10mm"/>

    </fo:simple-page-master>
  </fo:layout-master-set>


  <fo:page-sequence master-reference="page">
    <fo:static-content flow-name="xsl-region-before">

      <!--HEADER-->

      <fo:block border-before-style="solid"
		border-after-style="solid">
	<fo:table table-layout="auto">
	  <fo:table-column column-number="1" column-width="50%"/>
	  <fo:table-column column-number="2" column-width="50%"/>

	  <fo:table-body>
	    <fo:table-row>
	      <fo:table-cell>
		<fo:block>
		  <fo:external-graphic src="images/sigla.png" content-height="20mm"/>
		</fo:block>
	      </fo:table-cell>
	      <fo:table-cell font-size="6pt" display-align="center">
		<fo:block text-align="end">BUCURESTI B-dul PRECIZIEI nr. 32, Sector 6</fo:block>
		<fo:block text-align="end">Contractari: Tel/Fax: 316.05.90 Tel: 316.18.56; 316.05.88</fo:block>
		<fo:block text-align="end">Secretariat: Tel/Fax: 316.05.88; Tel: 316.39.57</fo:block>
		<fo:block text-align="end">Centrala: Tel: 316.05.78 Dep. Economic: Tel: 317.25.40</fo:block>
		<fo:block text-align="end">Magazin OBOR: sos. Mihai Bravu 6, Tel/Fax: 252.49.67</fo:block>
		<fo:block text-align="end">Web: http://www.setumsa.ro; e-mail: desfacere@setum.ro contractari@setum.ro</fo:block>
		<fo:block text-align="end">Capital social: 14,10 miliarde lei</fo:block>
	      </fo:table-cell>
	    </fo:table-row>
	  </fo:table-body>
	</fo:table>
      </fo:block>


      <!--/HEADER-->

    </fo:static-content>

    <fo:static-content flow-name="xsl-region-after">
      <!--FOOTER-->
      <fo:block text-align="end" font-size="6pt"
		border-before-style="solid">Pagina <fo:page-number/></fo:block>
      <!--/FOOTER-->
    </fo:static-content>


    <fo:flow flow-name="xsl-region-body">
      <!-- date generale despre oferta (numar, data, nume client, etc.) -->
      <fo:block>
	<fo:table width="90%">
	  <fo:table-column column-number="1" column-width="30%"/>
	  <fo:table-column column-number="2" column-width="70%"/>

	  <fo:table-body>
	    <fo:table-row>
	      <fo:table-cell column-number="1" border-style="solid" font-weight="bold">
		<fo:block>Oferta Nr:</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" border-style="solid">
		<fo:block>${doc["child::response/child::record/child::field[attribute::name='no']"]}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row>
	      <fo:table-cell column-number="1" border-style="solid" font-weight="bold">
		<fo:block>Catre:</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" border-style="solid">
		<fo:block>${doc["child::response/child::record/child::field[attribute::name='clientName']"]}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row>
	      <fo:table-cell column-number="1" border-style="solid" font-weight="bold">
		<fo:block>Din data:</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" border-style="solid">
		<fo:block>${doc["child::response/child::record/child::field[attribute::name='dateFrom']"]} (an-luna-zi)</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row>
	      <fo:table-cell column-number="1" border-style="solid" font-weight="bold">
		<fo:block>Valabilitate pana:</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" border-style="solid">
		<fo:block>${doc["child::response/child::record/child::field[attribute::name='dateTo']"]} (an-luna-zi)</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row>
	      <fo:table-cell column-number="1" border-style="solid" font-weight="bold">
		<fo:block>Observatii:</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" border-style="solid">
		<fo:block>${doc["child::response/child::record/child::field[attribute::name='description']"]}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	  </fo:table-body>
	</fo:table>
      </fo:block>



      <!-- liniile ofertei -->
      <#assign lineno=1>
      <#list doc["child::response/child::record/child::field[attribute::name='lines']/child::record"] as record>
      <fo:block text-align="justified">
	${lineno}. ${record["child::field[attribute::name='productCategory']"]} - ${record["child::field[attribute::name='productCode']"]} 
	<fo:leader leader-pattern="dots" leader-length.optimum="100%"/>${record["child::field[attribute::name='vatPrice']"]}
      </fo:block>
      <#switch record["child::field[attribute::name='product']/child::record/child::field[attribute::name='category.id']"]?number>
      <#case 9990>
      <#case 9993>
      <fo:block>
	<#visit record["child::field[attribute::name='product']/child::record"]>
      </fo:block>
      </#switch>

      <#assign lineno = lineno + 1>
      </#list>




    </fo:flow>


  </fo:page-sequence>


</fo:root>


<!--
<#macro "record">
   <#switch .node["child::field[attribute::name='category.id']/child::text()"]?number>
   <#case 9990>
   ${search(.node?parent, "material", .node["child::field[attribute::name='material']"])?cap_first}, 
                Nr canate:${.node["child::field[attribute::name='k']"]},
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
		Sisteme:
		<#if .node["child::field[attribute::name='broascaBuc']"] &gt; 0>
		Broasca ${search(.node?parent,"broascaId",.node["child::field[attribute::name='broascaId']"])} - ${.node["child::field[attribute::name='broascaBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='cilindruBuc']"] &gt; 0>
		Cilindru ${search(.node?parent,"cilindruId",.node["child::field[attribute::name='cilindruId']"])} - ${.node["child::field[attribute::name='cilindruBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='copiatCheieBuc']"] &gt; 0>
		Copiat chei ${search(.node?parent,"copiatCheieId",.node["child::field[attribute::name='copiatCheieId']"])} - ${.node["child::field[attribute::name='copiatCheieBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='vizorBuc']"] &gt; 0>
		Vizor ${search(.node?parent,"vizorId",.node["child::field[attribute::name='vizorId']"])} - ${.node["child::field[attribute::name='vizorBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='vizorBuc']"] &gt; 0>
		Vizor ${search(.node?parent,"vizorId",.node["child::field[attribute::name='vizorId']"])} - ${.node["child::field[attribute::name='vizorBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='sildBuc']"] &gt; 0>
		Sild ${search(.node?parent,"sildId",.node["child::field[attribute::name='sildId']"])} - ${.node["child::field[attribute::name='sildBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='rozetaBuc']"] &gt; 0>
		Rozeta ${search(.node?parent,"rozetaId",.node["child::field[attribute::name='rozetaId']"])} - ${.node["child::field[attribute::name='rozetaBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='manerBuc']"] &gt; 0>
		Maner ${search(.node?parent,"manerId",.node["child::field[attribute::name='manerId']"])} - ${.node["child::field[attribute::name='manerBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='yalla1Buc']"] &gt; 0>
		Yalla ${search(.node?parent,"yalla1Id",.node["child::field[attribute::name='yalla1Id']"])} - ${.node["child::field[attribute::name='yalla1Buc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='yalla2Buc']"] &gt; 0>
		Yalla ${search(.node?parent,"yalla2Id",.node["child::field[attribute::name='yalla2Id']"])} - ${.node["child::field[attribute::name='yalla2Buc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='baraAntipanicaBuc']"] &gt; 0>
		Bara antipanica ${search(.node?parent,"baraAntipanicaId",.node["child::field[attribute::name='baraAntipanicaId']"])} - ${.node["child::field[attribute::name='baraAntipanicaBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='manerSemicilindruBuc']"] &gt; 0>
		Maner semicilindru ${search(.node?parent,"manerSemicilindruId",.node["child::field[attribute::name='manerSemicilindruId']"])} - ${.node["child::field[attribute::name='manerSemicilindruBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='selectorOrdineBuc']"] &gt; 0>
		Selector ordine ${search(.node?parent,"selectorOrdineId",.node["child::field[attribute::name='selectorOrdineId']"])} - ${.node["child::field[attribute::name='selectorOrdineBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='amortizorBuc']"] &gt; 0>
		Amortizor ${search(.node?parent,"amortizorId",.node["child::field[attribute::name='amortizorId']"])} - ${.node["child::field[attribute::name='amortizorBuc']"]} buc.
		<#if .node["child::field[attribute::name='alteSisteme1Buc']"] &gt; 0>
		${search(.node?parent,"alteSisteme1Id",.node["child::field[attribute::name='alteSisteme1Id']"])} - ${.node["child::field[attribute::name='alteSisteme1Buc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='alteSisteme2Buc']"] &gt; 0>
		${search(.node?parent,"alteSisteme2Id",.node["child::field[attribute::name='alteSisteme2Id']"])} - ${.node["child::field[attribute::name='alteSisteme2Buc']"]} buc.
		</#if>


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
-->



