function generatePDF(type, canvas) {
    var doc = new jsPDF('landscape', 'px', 'a4', true);
    var width = doc.internal.pageSize.width;
    var height = canvas.height * width / canvas.width;
    doc.addImage(canvas.toDataURL(), 'PNG', 15, 15, width - 30, height - 15, undefined, 'MEDIUM');
    var filename = (new Date()).toISOString().slice(0,10) + '-avalon-' + type + '.pdf';
    doc.save(filename);
}