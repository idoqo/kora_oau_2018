package xyz.mchl.ferapid;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xyz.mchl.ferapid.junk.Utils;
import xyz.mchl.ferapid.persistence.QRCode;

public class QrCodeListAdapter extends RecyclerView.Adapter<QrCodeListAdapter.QrCodeViewHolder>
{
    private Context context;
    private List<QRCode> qrCodeList;
    private View.OnLongClickListener longClickListener;

    public QrCodeListAdapter(Context ctx, List<QRCode> codeList, View.OnLongClickListener longClickListener) {
        this.context = ctx;
        this.qrCodeList = codeList;
        this.longClickListener = longClickListener;
    }

    public QrCodeListAdapter(Context context, List<QRCode> codeList) {
        this.context = context;
        this.qrCodeList = codeList;
        this.longClickListener = null;
    }

    @Override
    public QrCodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.qrcode_item, parent, false);
        return new QrCodeViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return qrCodeList.size();
    }

    @Override
    public void onBindViewHolder(QrCodeViewHolder holder, int position) {
        QRCode qrCode = qrCodeList.get(position);
        try {
            Bitmap qrImage = Utils.loadImageFromStorage(context, qrCode.getQrImagePath());
            if (qrImage != null) {
                holder.qrCodeView.setImageBitmap(qrImage);
            }
        } catch (Exception e) {

        }
        holder.accountNumberView.setText(qrCode.getAccountNumber());
        holder.bankNameView.setText(qrCode.getBankName());
        //holder.amountView.setText(qrCode.getAmount());
    }

    public void addItems(List<QRCode> qrCodes) {
        this.qrCodeList = qrCodes;
        notifyDataSetChanged();
    }

    static class QrCodeViewHolder extends RecyclerView.ViewHolder {
        private ImageView qrCodeView;
        private TextView bankNameView;
        private TextView accountNumberView;
        private TextView amountView;

        QrCodeViewHolder(View view) {
            super(view);
            qrCodeView = view.findViewById(R.id.qr_photo);
            bankNameView = view.findViewById(R.id.bank_name);
            amountView = view.findViewById(R.id.amount);
            accountNumberView = view.findViewById(R.id.account_number);
        }
    }
}
